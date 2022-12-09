package iscte.server.controllers;

import iscte.server.Server;
import iscte.server.ServerService;
import iscte.server.ServerUtil;
import pt.iscte.Event;
import pt.iscte.PersonalCalendar;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
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
        Map<String, Object> response = process(paramsToProcess);

        return "ok";
    }

    @Override
    public Map<String, Object> process(Map<String, String> params) {
        int sYear, sMonth, sDay, eYear, eMonth, eDay;
        String[] startDateSplit = params.get("startDate").split("-");
        String[] endDateSplit = params.get("endDate").split("-");
        String[] users = params.get("users").split(",");
        Map<String, List<Event>> eventsToFindMeeting = new HashMap<>();

        // Make sure that the dates provided are numbers
        try {
            sYear = Integer.parseInt(startDateSplit[0]);
            sMonth = Integer.parseInt(startDateSplit[1]);
            sDay = Integer.parseInt(startDateSplit[2]);

            eYear = Integer.parseInt(endDateSplit[0]);
            eMonth = Integer.parseInt(endDateSplit[1]);
            eDay = Integer.parseInt(endDateSplit[2]);
        } catch (NumberFormatException e) {
            return buildResponseMap(null,"Something wrong with provided dates",false,true);
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
                    eventsToFindMeeting.put(user, calendars.get(user).getEventsBetweenTwoDates(startDate, endDate, params.get("timeOfDay")));
                }
            }
        }

        return findMeeting(eventsToFindMeeting);
    }

    private Map<String, Object> findMeeting(Map<String, List<Event>> events) {
        System.out.println(events);
        return null;
    }
}
