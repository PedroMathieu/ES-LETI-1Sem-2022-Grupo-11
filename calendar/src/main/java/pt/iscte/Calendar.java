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

/**
 * The calendar class represents all the events of a specific calendar owner
 * It parses all the data from the already converted JSON calendars in the 
 * calendars folder. 
 * To use more calendars simply download your calendar at fenix.iscte-iul.pt/
 * and convert it to JSON in https://ical-to-json.herokuapp.com/
 * This class will take care of the rest :)
 * 
 * @author Jose Soares
 */
public class Calendar {
    String id = "";
    String calendarFile = "";
    String calendarOwner = "";
    
    List<Event> events = new LinkedList<>();
    JSONParser parser = new JSONParser();

    public Calendar(String id, String calendarFile) {
        this.id = id;
        this.calendarFile = calendarFile;
        parseJsonCalendar();
    }

    /**
     * Gets data from JSON calendar file and processes it
     * by getting all the events in an list for easier
     * data manipulation and the calendar owner
     */
    private void parseJsonCalendar() {
        try (Reader reader = new FileReader(calendarFile)) {
            List<JSONObject> eventListJson = new LinkedList<>();

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
            
            // Add all the events to a JSONObject arraylist
            while (vEventsIterator.hasNext()) 
                eventListJson.add((JSONObject) vEventsIterator.next());

            // Convert all events from JSON to Event
            this.events = convertEventListFromJSON(eventListJson);

        } catch (Exception e) {
            System.err.println("Couldn't read file " + calendarFile);
        }        
    }

    /**
     * Converts all the events in JSON to Event objects
     * Makes it easier for searching
     * @param eventListJson list of events as JSON objects
     * @return a List of all the events as Event objects
     */
    private List<Event> convertEventListFromJSON(List<JSONObject> eventListJson) {
        List<Event> eventsToReturn = new LinkedList<>();

        for (JSONObject j : eventListJson) {
            eventsToReturn.add(new Event(
                    calendarOwner, 
                    (String) j.get("summary"), 
                    (String) j.get("dtstart"),
                    (String) j.get("dtend"))
            );
        }
        
        return eventsToReturn;
    }

    /**
     * Gets the list that contains all the events of the 
     * respective calendar
     * 
     * @return a List of events as Event objects
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * TODO: Get events between timestamp IN A DAY method
     * The return statement must have: calendar owner, event name, tStart and tEnd. 
     * We are checking for events in a SINGLE day, so we don't have to return the day
     */
}
