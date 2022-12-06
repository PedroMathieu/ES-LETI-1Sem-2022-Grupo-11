package iscte.server.controllers;

import iscte.server.ServerUtil;
import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FindMeetingController extends Controller {

    @Override
    public Object handle(Request req, Response res) {
        Map<String, String> paramsToProcess = new HashMap<>();
        paramsToProcess.put("startDate", req.queryParams("startDate"));
        paramsToProcess.put("endDay", req.queryParams("endDay"));
        paramsToProcess.put("duration", req.queryParams("duration"));
        paramsToProcess.put("timeOfDay", req.queryParams("timeOfDay"));
        paramsToProcess.put("users", req.queryParams("users"));
        Map<String, Object> response = process(paramsToProcess);

        return "ok";
    }

    @Override
    public Map<String, Object> process(Map<String, String> params) {
        /**
         * TODO:
         * - dont forget to:
         *      validate both dates received
         *      validate users
         *      validate time of day (create a range that represents morning and afternoon
         * - use of ServerUtil and ServerService validators for dates and owners
         * - use PersonalCalendar getEventsBetweenTwoDates() to get the events of all users
         * - then do the processing required!
         *
         * - this function only returns the output of the Controllers buildResponseMap!
         */
        return null;
    }

}
