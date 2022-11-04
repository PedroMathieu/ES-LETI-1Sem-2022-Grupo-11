package pt.iscte;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.exit;

public class App  {
    // List of calendar objects
    private static final Map<String, PersonalCalendar> personalCalendarObjects = new HashMap<>();
    private static final Server server = new Server(personalCalendarObjects);

    /**
     * Loads calendars present in the calendars folders and creates
     * PersonalCalendar objects from them. It puts them in a global map
     * with a unique ID, to be used later on.
     *
     * @throws SecurityException if we don't have read permissions to the directory
     * @throws NullPointerException if the calendars folder path is wrong or no calendars in it
     */
    private static void loadCalendars () {
        File folder = new File(System.getProperty("user.dir") + "/calendars");
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

    public static Map<String, PersonalCalendar> getPersonalCalendarObjects() {
        return personalCalendarObjects;
    }

    public static void main(String[] args) {
        loadCalendars();
    }
}
