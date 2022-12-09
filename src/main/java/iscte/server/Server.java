package iscte.server;

import iscte.server.controllers.FindMeetingController;
import pt.iscte.PersonalCalendar;
import iscte.server.controllers.CalendarUploadController;
import iscte.server.controllers.Controller;
import iscte.server.controllers.GetEventsController;
import spark.ModelAndView;
import spark.servlet.SparkApplication;
import spark.template.velocity.VelocityTemplateEngine;

import java.util.*;

import static spark.Spark.*;

/**
 * Server class responsible for setting up the server and
 * its endpoints.
 *
 * @author Jose Soares
 */
public class Server implements SparkApplication {
    private static Map<String, PersonalCalendar> personalCalendarObjects;
    private Controller cEvents = new GetEventsController(getPersonalCalendarObjects());
    private Controller cUpload = new CalendarUploadController();
    private Controller cMeeting = new FindMeetingController(getPersonalCalendarObjects());

    public Server() {
    }

    /**
     * Updates currently loaded personalCalendars.
     *
     * @param newPCalendars updated personalCalendars
     */
    public static void updatePersonalCalendarObjects(Map<String, PersonalCalendar> newPCalendars) {
        personalCalendarObjects = newPCalendars;
    }

    /**
     * Get personal calendar objects
     *
     * @return map with personal calendar objects and owners
     */
    public static Map<String, PersonalCalendar> getPersonalCalendarObjects() {
        return personalCalendarObjects;
    }

    /**
     * Sets up all the routes to all endpoints.
     */
    private void setupEndpoints() {
        System.out.println("[SERVER] setting up routes");

        notFound((req, res) -> {
            Map<String, Object> data = new HashMap<>();

            data.put("message", "What you're looking for can't be found!");
            data.put("messageSrc", "404 Not found");
            data.put("hasMessage", true);

            return new VelocityTemplateEngine().render(
                    new ModelAndView(data, "calendarWeb/ErrorPage.vm"));
        });

        get("/", (req, res) -> {
            Map<String, List<String>> data = new HashMap<>();
            List<String> ownerList = new ArrayList<>(getPersonalCalendarObjects().keySet());

            data.put("owners", ownerList);

            return new VelocityTemplateEngine().render(
                    new ModelAndView(data, "calendarWeb/Site.html"));
        });

        get("/insertCalendars", (req, res) -> {
            res.redirect("/CalendarInput.html");
            return null;
        });

        post("/uploadCalendarLink", (req, res) -> cUpload.handle(req, res));

        get("/personalCalendar/:operation/:userId/:year/:month/:day", (req, res) -> cEvents.handle(req, res));

        post("/findMeeting", (req, res) -> cMeeting.handle(req, res));
    }

    @Override
    public void init() {
        ServerService.deleteTempIcsFiles();
        ServerService.loadCalendars();
        port(4444);
        staticFiles.location("/calendarWeb");
        setupEndpoints();
        System.out.println("[SERVER] alive at: http://localhost:4444");
    }
}
