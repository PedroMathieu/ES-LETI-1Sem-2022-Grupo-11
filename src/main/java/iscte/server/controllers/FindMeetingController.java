package iscte.server.controllers;

import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FindMeetingController extends Controller {

    /**
     * Builds parameters requested. Convert dates to each
     * individual element (year, month and day).
     * @param req Spark request object
     * @return map with processed params
     */
    private Map<String, String> buildMeetingParams(Request req) {
        Map<String, String> paramsToProcess = new HashMap<>();
        System.out.println(req.queryParams());
        for (String param : req.queryParams()) {

            if (param.equals("startDay")) {
                String[] date = req.queryParams(param).split("-");
                paramsToProcess.put("startYear", date[0]);
                paramsToProcess.put("startMonth", date[1]);
                paramsToProcess.put("startDay", date[2]);
            } else if (param.equals("endDay")) {
                String[] date = req.queryParams(param).split("-");
                paramsToProcess.put("endYear", date[0]);
                paramsToProcess.put("endMonth", date[1]);
                paramsToProcess.put("endDay", date[2]);
            } else {
                paramsToProcess.put(param, req.queryParams(param));
            }
        }

        return paramsToProcess;
    }

    /**
     * Make sure all needed params are available
     * @param params set of params received
     * @return true if all params exist
     */
    private boolean validateParams(Set<String> params) {
        if (params.contains("duration") &&
                params.contains("startMonth") &&
                params.contains("startDay") &&
                params.contains("endDay") &&
                params.contains("startYear") &&
                params.contains("endMonth") &&
                params.contains("endYear") &&
                params.contains("timeOfDay") &&
                params.contains("users")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Object handle(Request req, Response res) {
        Map<String, String> processParams = buildMeetingParams(req);

        if (validateParams(processParams.keySet())) {
            Map<String, Object> response = process(processParams);
            return "ok";
        } else {
            return "ERROR";
        }
    }

    @Override
    public Map<String, Object> process(Map<String, String> params) {
        return null;
    }

}
