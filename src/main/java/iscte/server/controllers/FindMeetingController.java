package iscte.server.controllers;

import iscte.server.Server;
import iscte.server.ServerService;
import iscte.server.ServerUtil;
import org.eclipse.jetty.util.ajax.JSON;
import org.json.simple.JSONObject;
import pt.iscte.Event;
import pt.iscte.PersonalCalendar;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class FindMeetingController extends Controller {
    private List<String> usersThatCanHaveTheMeeting = new ArrayList<>();
    int biggestNumberOfUsers = 0;
    private LocalDate dayOfMeeting;
    private LocalTime startOfMeetingTime;
    private LocalTime endOfMeetingTime;
    private Map<String, PersonalCalendar> calendars = new HashMap<>();

    public FindMeetingController(Map<String, PersonalCalendar> calendars) {
        this.calendars = calendars;
    }

    @Override
    public Object handle(Request req, Response res) {
        Map<String, String> paramsToProcess = new HashMap<>();
        this.calendars = Server.getPersonalCalendarObjects(); // if in handle, server is running

        paramsToProcess.put("startDate", req.queryParams("startDate"));
        paramsToProcess.put("endDate", req.queryParams("endDate"));
        paramsToProcess.put("duration", req.queryParams("duration"));
        paramsToProcess.put("timeOfDay", req.queryParams("timeOfDay"));
        paramsToProcess.put("users", req.queryParams("users"));

        Map<String, Object> response = process(paramsToProcess);

        res.type("application/json");

        if ((boolean) response.get("gotError")) {
            JSONObject jsonToSend = new JSONObject();
            jsonToSend.put("error", "oops");
            return jsonToSend;

        } else
            return (JSONObject) response.get("dataToSend");
    }

    @Override
    public Map<String, Object> process(Map<String, String> params) {
        int sYear, sMonth, sDay, eYear, eMonth, eDay;
        String[] startDateSplit = params.get("startDate").split("-");
        String[] endDateSplit = params.get("endDate").split("-");
        String[] users = params.get("users").split(",");
        Map<String, List<Event>> eventsToFindMeeting = new HashMap<>();

        if (users.length < 2)
            return buildResponseMap(null, "Need 2 or more users to find a meeting", false, true);

        // Make sure that the dates provided are numbers
        try {
            sYear = Integer.parseInt(startDateSplit[0]);
            sMonth = Integer.parseInt(startDateSplit[1]);
            sDay = Integer.parseInt(startDateSplit[2]);

            eYear = Integer.parseInt(endDateSplit[0]);
            eMonth = Integer.parseInt(endDateSplit[1]);
            eDay = Integer.parseInt(endDateSplit[2]);
        } catch (NumberFormatException e) {
            return buildResponseMap(null, "Something wrong with provided dates", false, true);
        }

        for (String user : users) {
            if (!ServerUtil.validateDateParams(sYear, sMonth, sDay) ||
                    !ServerService.validateOwner(user, calendars) ||
                    !ServerUtil.validateDateParams(eYear, eMonth, eDay)) {
                return buildResponseMap(null, "Parameters contain problems", false, true);

            } else {
                LocalDate startDate = LocalDate.of(sYear, sMonth, sDay);
                LocalDate endDate = LocalDate.of(eYear, eMonth, eDay);

                if (startDate.isAfter(endDate)) {
                    return buildResponseMap(null, "Incorrect date interval", false, true);
                } else {
                    eventsToFindMeeting.put(user,
                            calendars.get(user).getEventsBetweenTwoDates(startDate, endDate, params.get("timeOfDay")));
                }
            }
        }

        return findMeeting(eventsToFindMeeting, Integer.parseInt(params.get("duration")));
    }

    private Map<String, Object> findMeeting(Map<String, List<Event>> events, int duration) {
        // list of all users
        Set<String> listOfUsers = events.keySet();
        // goes through the list of users and gets the list of events for each of them
        for (String user : listOfUsers) {
            // stores the list of events for the current user
            List<Event> listOfEvents = events.get(user);
            // for each user checks all events and
            for (Event event : listOfEvents) {
                dayOfMeeting = event.getDayOfEvent();
                startOfMeetingTime = event.getEventTimeEnd();
                endOfMeetingTime = startOfMeetingTime.plus(duration, ChronoUnit.MINUTES);
                if (meetingFits(listOfEvents)) {
                    usersThatCanHaveTheMeeting.add(user);
                    checkOtherUsers(listOfUsers, user, events);
                }
                if (usersThatCanHaveTheMeeting.size() > biggestNumberOfUsers)
                    biggestNumberOfUsers = usersThatCanHaveTheMeeting.size();
            }
        }
        
        JSONObject timeslot = new JSONObject();
        timeslot.put("timeslotStart", startOfMeetingTime.toString());
        timeslot.put("timeslotEnd", endOfMeetingTime.toString());
        timeslot.put("dayOfMeeting", dayOfMeeting.toString());

        return buildResponseMap(timeslot, "", true, false);
    }

    private boolean meetingFits(List<Event> listOfEvents) {
        for (Event event : listOfEvents) {
            if ((event.getEventTimeStart().compareTo(startOfMeetingTime) > 0)
                    && (event.getEventTimeStart().compareTo(endOfMeetingTime) < 0)) {
                return false;
            }
        }
        return true;
    }

    private void checkOtherUsers(Set<String> listOfUsers, String currentUser, Map<String, List<Event>> events) {
        for (String user : listOfUsers) {
            if (!user.equals(currentUser)) {
                List<Event> listOfEventsOfUser = events.get(user);
                if (meetingFits(listOfEventsOfUser)) {
                    usersThatCanHaveTheMeeting.add(user);
                }
            }
        }
    }
}
