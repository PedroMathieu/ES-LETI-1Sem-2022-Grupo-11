package pt.iscte;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.servlet.SparkApplication;
import spark.template.velocity.VelocityTemplateEngine;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static java.lang.System.exit;
import static spark.Spark.*;


/**
 * The server class is responsible for handling requests from the front end.
 * The requests are mainly Event requests.
 *
 * @author Jose Soares
 */
public class Server implements SparkApplication {
    private static final Map<String, PersonalCalendar> personalCalendarObjects = new HashMap<>();
    public Server() {
        init();
    }

    /**
     * Checks if date is valid. By checking year, month and day ranges.
     * Also checks if the given days are in the range of the given month.
     *
     * @param year requested year to validate
     * @param month requested month to validate
     * @param day request day to validate
     * @return true if date is valid, false otherwise
     */
    private boolean validateDateParams(int year, int month, int day) {
        System.out.println("[SERVER] validating date");
        // Check the ranges of month and year
        if (year < 1000 || year > 3000 || month == 0 || month > 12)
            return false;

        int[] monthLength = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

        // Adjust for leap years
        if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
            monthLength[1] = 29;

        // Check the range of the day
        return day > 0 && day <= monthLength[month - 1];
    }

    /**
     * Checks if there's a calendar with the specified owner
     *
     * @param rOwner name of calendar owner to check
     * @return true if there's a calendar with that owner, false otherwise
     */
    private boolean validateOwner(String rOwner) {
        System.out.println("[SERVER] checking if calendar owner exists");
        for (String owner : personalCalendarObjects.keySet())
            if (rOwner.equals(owner)) return true;
        return false;
    }

    /**
     * Builds event data in JSON to send it to the front end.
     *
     * @param rOwner calendar owner, to get its calendar
     * @param dateRequested requested date to get events
     * @return JSON object to send to front end
     */
    private JSONObject buildEventsInJson(String rOwner, LocalDate dateRequested) {
        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        for (Event e : personalCalendarObjects.get(rOwner).getEventsInADay(dateRequested))
            jsonArray.add(e.convertEventToJson());

        json.put("events", jsonArray);
        return json;
    }

    /**
     * Renders a custom error template
     *
     * @param message message to send
     * @return JSONObject with message to send
     */
    public Object senErrorToUser(String message) {
        System.out.println("[SERVER ERROR] something went wrong -> " + message);
        Map<String, Object> data = new HashMap<>();

        data.put("message", message);
        data.put("messageSrc", "Server Error");
        data.put("hasMessage", true);

        return new VelocityTemplateEngine().render(
                new ModelAndView(data, "calendarWeb/ErrorPage.vm")
        );
    }

    /**
     * Generates a random string of letters to be used to create
     * a temp file with a random name. Just to avoid duplicate files
     * when uploading multiple files. This a scalable way to upload calendars.
     *
     * @return string with random characters
     */
    private String generateRandomTempName() {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }

        return buffer.toString();
    }

    /**
     * Uploads a .ics calendar to server so that it can be used by the parser
     *
     * @param req Spark request object, contains parameters info
     * @param res Spark response object
     * @return response to give
     */
    private Object uploadCalendarToServer(Request req, Response res) throws IOException {
        String calendarUrl = req.queryParams("calendar-link-input").trim();

        // Checks if the link protocol is webcal and then it changes it to https for download
        System.out.println("[SERVER] Validating URL protocol");
        if (!(calendarUrl.startsWith("webcal"))) {
            return senErrorToUser("Please make sure the url is webcal://");
        }

        calendarUrl = calendarUrl.replace("webcal://", "https://");
        System.out.println("[SERVER] Starting upload of " + calendarUrl + " to the server!");

        String tempFileName = generateRandomTempName() + "_temp.ics";
        String tempFilePath = System.getProperty("user.dir") + "/calendars/icsFiles/" + tempFileName;

        // Reading calendar data into temp file
        System.out.println("[SERVER] Getting calendar data stream");
        BufferedInputStream bis = new BufferedInputStream(new URL(calendarUrl).openStream());

        System.out.println("[SERVER] Creating calendar output file");
        try (FileOutputStream fis = new FileOutputStream(tempFilePath)) {
            byte[] buffer = new byte[1024];
            int count = 0;

            System.out.println("[SERVER] Writing calendar to file");
            while ((count = bis.read(buffer, 0, 1024)) != -1)
                fis.write(buffer, 0, count);

            fis.close();
            bis.close();
        } catch (FileNotFoundException e) {}

        // Parse the .ics calendar
        new Parser().initiateCalendar(tempFileName);

        // Delete .ics temp file
        File f = new File(tempFilePath);
        if (f.delete())
            System.out.println("[SERVER] " + f.getName() + " deleted temp file");
        else
            System.err.println("[SERVER] couldn't delete temp file");

        // Reload calendars
        loadCalendars();

        res.redirect("/");
        return null;
    }

    /**
     * Route of getEventsByDay. Gets all the events from the calendar.
     * Given the owner of the calendar by giving the date
     *
     * @param req Spark request object, contains parameters info
     * @param res Spark response object
     * @return response to give
     */
    private Object getEventsByDayRoute(Request req, Response res) {
        int rYear, rMonth, rDay;
        List<String> requestedOwners = List.of(req.params("userId").split("-"));
        JSONObject jsonEvents = new JSONObject();
        Map<String, JSONObject> data = new HashMap<>();
        System.out.println(requestedOwners);


        if (requestedOwners.size() == 0) {} // render main page with a message to specify at least one user
        else {
            for (String rOwner : requestedOwners) {
                System.out.println("[SERVER] getting events of " + rOwner);

                // Make sure that the date provided are numbers
                System.out.println("[SERVER] converting date");
                try {
                    rYear = Integer.parseInt(req.params("year"));
                    rMonth = Integer.parseInt(req.params("month"));
                    rDay = Integer.parseInt(req.params("day"));
                } catch (NumberFormatException e) {
                    return senErrorToUser("Year, month or day is not a number");
                }

                // Validate date params and calendar owner
                if (!validateDateParams(rYear, rMonth, rDay) || !validateOwner(rOwner)) {
                    return senErrorToUser("Parameters contain problems");
                }

                System.out.println("[SERVER] getting events");
                LocalDate dateRequested = LocalDate.of(rYear, rMonth, rDay);

                jsonEvents.put(rOwner, buildEventsInJson(rOwner, dateRequested));
            }
        }

        data.put("events", jsonEvents);

        return new VelocityTemplateEngine().render(
                new ModelAndView(data, "calendarWeb/CalendarDaily.html")
        );
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
                    new ModelAndView(data, "calendarWeb/ErrorPage.vm")
            );
        });

        // Using a template engine only in this endpoint. At least for now...
        get("/", (req, res) -> {
            Map<String, List<String>> data = new HashMap<>();
            List<String> ownerList = new ArrayList<>(getPersonalCalendarObjects().keySet());

            data.put("owners", ownerList);

            return new VelocityTemplateEngine().render(
                    new ModelAndView(data, "calendarWeb/Site.html")
            );
        });

        get("/insertCalendars", (req, res) -> {
           res.redirect("/CalendarInput.html");
           return null;
        });

        post("/uploadCalendarLink", this::uploadCalendarToServer);

        get("/personalCalendar/:userId/:year/:month/:day", this::getEventsByDayRoute);
    }

    /**
     * Loads calendars present in the calendars folders and creates
     * PersonalCalendar objects from them. It puts them in a global map
     * with a unique ID, to be used later on.
     *
     * @throws SecurityException if we don't have read permissions to the directory
     * @throws NullPointerException if the calendars folder path is wrong or no calendars in it
     */
    private void loadCalendars () {
        File folder = new File(System.getProperty("user.dir") + "/calendars/jsonFiles/");
        try {
            for (File fileEntry : folder.listFiles()) {
                PersonalCalendar personalCalendarHandler = new PersonalCalendar(fileEntry.getAbsolutePath());
                personalCalendarObjects.put(personalCalendarHandler.getCalendarOwner(), personalCalendarHandler);
            }
        } catch (NullPointerException e) {
            System.err.println("Could not read anything in the folder " + folder.getAbsolutePath());
            exit(0);
        }
    }

    /**
     * Get personal calendar objects
     * @return map with personal calendar objects and owners
     */
    public static Map<String, PersonalCalendar> getPersonalCalendarObjects() {
        return personalCalendarObjects;
    }

    @Override
    public void init() {
        loadCalendars();
        port(4444);
        staticFiles.location("/calendarWeb");
        setupEndpoints();
        System.out.println("[SERVER] alive at: http://localhost:4444");
    }
}
