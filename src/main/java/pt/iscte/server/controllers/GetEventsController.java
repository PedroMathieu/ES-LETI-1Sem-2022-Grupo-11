package pt.iscte.server.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pt.iscte.PersonalCalendar;
import pt.iscte.server.Server;
import pt.iscte.server.ServerService;
import pt.iscte.server.ServerUtil;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.template.velocity.VelocityTemplateEngine;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Get events controller. This controller is responsible for
 * getting events data. Either the events themselves or the
 * number of events.
 *
 * @author Jose Soares
 */
public class GetEventsController extends Controller {
    private Map<String, PersonalCalendar> calendars = new HashMap<>();

    public GetEventsController(Map<String, PersonalCalendar> calendars) {
        this.calendars = calendars;
    }

    @Override
    public Object handle(Request req, Response res) {
        Map<String, JSONObject> data = new HashMap<>();
        this.calendars = Server.getPersonalCalendarObjects(); // Use in memory loaded calendars, updated everytime

        Map<String, Object> response = process(req.params());

         // If the operation was numbers, return json. Else return template with events
        if ((boolean) response.get("gotError"))
            return response.get("error");

        if ((boolean) response.get("contextJson")) {
            res.header("Content-Type", "application/json");
            return (JSONObject) response.get("dataToSend");

        } else {
            data.put("events", (JSONObject) response.get("dataToSend"));
            return new VelocityTemplateEngine().render(
                    new ModelAndView(data, "calendarWeb/CalendarDaily.html"));
        }
    }

    @Override
    public Map<String, Object> process(Map<String, String> params) {
        int rYear = -1, rMonth = -1, rDay = -1;
        List<String> requestedOwners = List.of(params.get(":userid").split("-"));
        JSONObject jsonEvents = new JSONObject();
        String operation = params.get(":operation");
        boolean contextJson = false;

        // Make sure that the date provided are numbers
        try {
            rYear = Integer.parseInt(params.get(":year"));
            rMonth = Integer.parseInt(params.get(":month"));
            rDay = Integer.parseInt(params.get(":day"));
        } catch (NumberFormatException e) {
            return buildResponseMap(null,"Year, month or day is not a number",false,true);
        }

        for (String rOwner : requestedOwners) {

            // Validate date params and calendar owner
            if (!ServerUtil.validateDateParams(rYear, rMonth, rDay) || !ServerService.validateOwner(rOwner, calendars)) {
                return buildResponseMap(null, "Parameters contain problems", false, true);
            }

            LocalDate dateRequested = LocalDate.of(rYear, rMonth, rDay);
            JSONObject requestedEvents = ServerService.buildEventsInJson(rOwner, dateRequested, calendars);
            JSONArray arrayOfEvents = (JSONArray) requestedEvents.get("events");

            // n is to get number of events, e is to get events
            if (operation.equals("e")) {
                jsonEvents.put(rOwner, requestedEvents);

            } else if (operation.equals("n")) {
                jsonEvents.put(rOwner, arrayOfEvents.size());
                contextJson = true;

            } else {
                return buildResponseMap(null, "Selected operation does not exist", false, true);
            }
        }

        return buildResponseMap(jsonEvents, "", contextJson, false);
    }
}
