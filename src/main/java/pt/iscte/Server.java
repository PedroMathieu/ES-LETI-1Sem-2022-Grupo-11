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
    private String serverPath;
    private static final Map<String, PersonalCalendar> personalCalendarObjects = new HashMap<>();
    private static final Parser parser = new Parser();
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
     * Sends a simple message in json format
     *
     * @param message message to send
     * @return JSONObject with message to send
     */
    public Object sendErrorInJson(String message) {
        JSONObject json = new JSONObject();
        json.put("error", message);
        return json;
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
            res.type("application/json");
            return sendErrorInJson("Please make sure the url is webcal://");
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
        parser.initiateCalendar(tempFileName);

        // Delete .ics temp file
        File f= new File(tempFilePath);
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
        String rOwner = req.params("userId");
        res.type("application/json");

        // Make sure that the date provided are numbers
        System.out.println("[SERVER] converting date");
        try {
            rYear = Integer.parseInt(req.params("year"));
            rMonth = Integer.parseInt(req.params("month"));
            rDay = Integer.parseInt(req.params("day"));
        } catch (NumberFormatException e) {
            return sendErrorInJson("Year, month or day is not a number");
        }

        // Validate date params and calendar owner
        if (!validateDateParams(rYear, rMonth, rDay) || !validateOwner(rOwner)) {
            return sendErrorInJson("Something wrong in parameters");
        }

        System.out.println("[SERVER] getting events");
        LocalDate dateRequested = LocalDate.of(rYear, rMonth, rDay);

        res.status(200);
        return buildEventsInJson(rOwner, dateRequested);
    }

    /**
     * Sets up all the routes to all endpoints.
     */
    private void setupEndpoints() {
        System.out.println("[SERVER] setting up routes");

        // Using a template engine only in this endpoint. At least for now...
        get("/", (req, res) -> {
            Map<String, List<String>> data = new HashMap<>();
            List<String> ownerList = new ArrayList<>(getPersonalCalendarObjects().keySet());
            System.out.println(ownerList);

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

        get("/getEvents/:userId/:year/:month/:day", this::getEventsByDayRoute);
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
        //personalCalendarObjects.clear();
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
        this.serverPath = "http://localhost:4444";
        System.out.println("[SERVER] alive at: http://localhost:4444");
    }
}
