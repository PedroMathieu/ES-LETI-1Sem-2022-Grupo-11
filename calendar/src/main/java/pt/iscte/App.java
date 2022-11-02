package pt.iscte;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App  {
    // Map of calendars (maps calendar id and calendar file name)
    private static Map<String, String> calendars = new HashMap<String, String>();
    // List of calendar objects
    private static List<PersonalCalendar> personalCalendarObjects = new ArrayList<PersonalCalendar>();

    /**
    * Loads json calendar files from calendars directory
    * Warning: this is not checking if there are calendars with the same name in the calendars folder
    *           that will be worked on in the future
    */
    private static void loadCalendars () {
        File folder = new File(System.getProperty("user.dir") + "/calendars");

        for (File fileEntry : folder.listFiles())
            buildCalendars(fileEntry.getName().substring(0,1), fileEntry, 1);
        
    }

    /**
    * Builds Calendar objects and appends a calendar id
    * and the respective calendar file location to the 
    * map of calendars to keep track of the existing 
    * calendars in an efficient way (map instead of going through a list)
    * 
    * Warning: this is not checking if there are calendars with the same name in the calendars folder
    *           that will be worked on in the future
    * 
    * @param  id unique id to be able to access each calendar without problems
    * @param  fileEntry calendar json file (file in OS)
    * @param  endIndex gets updated in a recursive way to find unique id
    */
    private static void buildCalendars(String id, File fileEntry, int endIndex) {
        if (calendars.containsKey(id)) {
            buildCalendars(fileEntry.getName().substring(0, ++endIndex), fileEntry, ++endIndex);
        } else {
            calendars.put(id, fileEntry.getAbsolutePath());
            personalCalendarObjects.add(new PersonalCalendar(id, fileEntry.getAbsolutePath()));
        }
    }

    public static void main( String[] args) {
        loadCalendars();
    }
}
