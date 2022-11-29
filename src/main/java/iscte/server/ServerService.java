package iscte.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pt.iscte.Event;
import pt.iscte.PersonalCalendar;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.*;

/**
 * This class is used by the server when something needs to get done.
 * For example, to load the calendars or to build something in JSON
 *
 * @author Jose Soares
 */
public class ServerService {

    /**
     * Builds event data in JSON to send it to the front end.
     *
     * @param rOwner        calendar owner, to get its calendar
     * @param dateRequested requested date to get events
     * @param calendars map containing calendars and their owners
     * @return JSON object to send to front end
     */
    public static JSONObject buildEventsInJson(String rOwner, LocalDate dateRequested, Map<String, PersonalCalendar> calendars) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        List<Event> events = calendars.get(rOwner).getEventsInADay(dateRequested);

        for (Event e : events)
            jsonArray.add(e.convertEventToJson());

        json.put("events", jsonArray);
        return json;
    }

    /**
     * Checks if there's a calendar with the specified owner
     *
     * @param rOwner name of calendar owner to check
     * @return true if there's a calendar with that owner, false otherwise
     */
    public static boolean validateOwner(String rOwner, Map<String, PersonalCalendar> calendars) {
        for (String owner : calendars.keySet())
            if (rOwner.equals(owner))
                return true;
        return false;
    }

    /**
     * Renders a custom error template
     *
     * @param message message to send
     * @return JSONObject with message to send
     */
    public static Object sendErrorToUser(String message) {
        System.out.println("[SERVER ERROR] something went wrong -> " + message);
        Map<String, Object> data = new HashMap<>();

        data.put("message", message);
        data.put("messageSrc", "Server Error");
        data.put("hasMessage", true);

        return new VelocityTemplateEngine().render(
                new ModelAndView(data, "calendarWeb/ErrorPage.vm"));
    }

    /**
     * Deletes all temporary ics files from icsFiles folder
     */
    public static void deleteTempIcsFiles() {
        File icsFolder = new File(System.getProperty("user.dir") + "/calendars/icsFiles/");

        try {
            for (File file : icsFolder.listFiles())
                file.delete();

        } catch (NullPointerException e) {
            System.err.println("Couldn't delete files in temp file");
        }
    }

    /**
     * Loads calendars present in the calendars folders and creates
     * PersonalCalendar objects from them. It puts them in a global map
     * with a unique ID, to be used later on.
     *
     * @throws SecurityException    if we don't have read permissions to the
     *                              directory
     * @throws NullPointerException if the calendars folder path is wrong or no
     *                              calendars in it
     * @return Map that connects a calendar and its owner string
     *
     */
    public static Map<String, PersonalCalendar> loadCalendars() {
        File folder = new File(System.getProperty("user.dir") + "/calendars/jsonFiles/");
        Map<String, PersonalCalendar> personalCalendarObjects = new HashMap<>();

        try {
            for (File fileEntry : folder.listFiles()) {
                PersonalCalendar personalCalendarHandler = new PersonalCalendar(fileEntry.getAbsolutePath());
                personalCalendarObjects.put(personalCalendarHandler.getCalendarOwner(), personalCalendarHandler);
            }
        } catch (NullPointerException e) {
            System.err.println("Could not read anything in the folder " + folder.getAbsolutePath());
            exit(0);
        }

        Server.updatePersonalCalendarObjects(personalCalendarObjects);
        return personalCalendarObjects;
    }
}
