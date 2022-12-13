package iscte.server.controllers;

import iscte.server.Server;
import iscte.server.ServerService;
import iscte.server.ServerUtil;
import pt.iscte.Event;
import pt.iscte.PersonalCalendar;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAmount;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class FindMeetingController extends Controller {
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

        return process(paramsToProcess);
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
        /**
         * Possible algorithm:
         * Go through all the events
         * For each event, check if there's any event within eventEnd+duration.
         * If there is not any event, check the same for all the other users 
         * If everyone is free in that timeblock (currEventEnd+duration), send the
         * timeblock found
         * If one or more user is not available, increment its unavailability timer. 
         * So that we can show which user is busier
         */
        Set<String> listOfUsers = events.keySet();
        for (String user : listOfUsers){
            List<Event> listOfEvents = events.get(user);
            for(Event event : listOfEvents){
                LocalTime endOfMeeting = event.getEventTimeEnd().plus(duration, ChronoUnit.MINUTES);
            }
        }
         
        return null;
    }

    private boolean 
}
