package pt.iscte;

import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Calendar {
    String id = "";
    String calendarFile = "";
    String calendarOwner = "";
    List<JSONObject> eventList = new LinkedList<>();
    JSONParser parser = new JSONParser();

    public Calendar(String id, String calendarFile) {
        this.id = id;
        this.calendarFile = calendarFile;
        parseJsonCalendar();
    }

    public String getId() {
        return id;
    }

    public String getCalendarFile() {
        return calendarFile;
    }

    /**
     * Gets data from JSON calendar file and processes it
     * by getting all the events in an list for easier
     * data manipulation and the calendar owner
     */
    private void parseJsonCalendar() {
        try (Reader reader = new FileReader(calendarFile)) {

            // Parse vcalendar JSON array
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONArray vcalendar = (JSONArray) jsonObject.get("vcalendar");
            List<JSONObject> vCalendarObjects = new ArrayList<>();
            Iterator<JSONObject> vCalendarIterator = vcalendar.iterator();
            
            // Add all vcalendar attributes to a list
            while (vCalendarIterator.hasNext()) 
                vCalendarObjects.add((JSONObject) vCalendarIterator.next());
            
            // From vcalendar, get the calendar owner (email) and the events
            calendarOwner = (String) vCalendarObjects.get(0).get("x-wr-calname");
            JSONArray vEvents = (JSONArray) vCalendarObjects.get(0).get("vevent");
            Iterator<JSONObject> vEventsIterator = vEvents.iterator();
            
            // Add all the events to a JSONObject arraylist for better data manipulation
            while (vEventsIterator.hasNext()) 
                eventList.add((JSONObject) vEventsIterator.next());

        } catch (Exception e) {
            System.err.println("Couldn't read file " + calendarFile);
        }
    }
}
