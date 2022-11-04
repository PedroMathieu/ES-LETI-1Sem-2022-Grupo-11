package pt.iscte;

import spark.Request;
import spark.Response;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

/**
 * The server class is responsible for handling requests from the front end.
 * The requests are mainly Event requests.
 *
 * @author Jose Soares
 */
public class Server {
    private Map<String, PersonalCalendar> personalCalendarObjects = new HashMap<>();

    public Server(Map<String, PersonalCalendar> personalCalendars) {
        this.personalCalendarObjects = personalCalendars;
        staticFiles.location("/calendarWeb");
        setupEndpoints();
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
        for (String owner : App.getPersonalCalendarObjects().keySet())
            if (rOwner.equals(owner)) return true;
        return false;
    }

    /**
     * Route of getEventsByDay. Gets all the events from the calendar.
     * Given the owner of the calendar by giving the date
     *
     * @param req Spark request object, contains parameters info
     * @param res Spark response object
     * @return response to give
     */
    public Object getEventsByDayRoute(Request req, Response res) {
        int rYear, rMonth, rDay;
        String rOwner = req.params("userId");

        // Make sure that the date provided are numbers
        System.out.println("[SERVER] converting date");
        try {
            rYear = Integer.parseInt(req.params("year"));
            rMonth = Integer.parseInt(req.params("month"));
            rDay = Integer.parseInt(req.params("day"));
        } catch (NumberFormatException e) {
            return "Year, month or day is not a number";
        }

        // Make sure that a calendar Owner was specifieds
        System.out.println("[SERVER] checking if calendar owner is present");
        if (rOwner.equals("")) return "Specify a calendar owner";

        // Validate date params and calendar owner
        if (!validateDateParams(rYear, rMonth, rDay) && !validateOwner(rOwner))
            return "Something wrong in parameters";

        System.out.println("[SERVER] getting events");
        LocalDate dateRequested = LocalDate.of(rYear, rMonth, rDay);

        //TODO: return events in JSON or any other readable format
        return personalCalendarObjects.get(rOwner).getEventsInADay(dateRequested);
    }

    /**
     * Sets up all the routes to all endpoints.
     */
    public void setupEndpoints() {
        System.out.println("[SERVER] setting up routes");

        get("/", (req, res) -> {
            res.redirect("/Site.html");
            return null;
        });

        get("/getEvents/:userId/:year/:month/:day", this::getEventsByDayRoute);
    }
}
