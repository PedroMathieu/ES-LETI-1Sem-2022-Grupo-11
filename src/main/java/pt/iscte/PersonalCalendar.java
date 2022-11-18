package pt.iscte;

import java.io.FileReader;
import java.io.Reader;
import java.time.LocalDate;
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
public class PersonalCalendar {
    String calendarFile = "";
    String calendarOwner = "";
    
    List<Event> events = new LinkedList<>();
    JSONParser parser = new JSONParser();

    public PersonalCalendar(String calendarFile) {
        this.calendarFile = calendarFile;
        parseJsonCalendar();
    }

    /**
     * Gets data from JSON calendar file and processes it
     * by getting all the events in a list for easier
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
            calendarOwner = ((String) vCalendarObjects.get(0).get("x-wr-calname")).split("@")[0];
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
     * Gets the calendar owner
     * @return username of email of calendar owner (its unique)
     */
    public String getCalendarOwner() {
        return calendarOwner;
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
     * Gets all the events happening in a day. This will be 
     * useful when drawing the calendar on the screen.
     * 
     * @param d targeted day
     * @return list of events for the current calendar in that day
     */
    public List<Event> getEventsInADay(LocalDate d) {
        List<Event> result = new ArrayList<>();
        for (Event e : events)
            if (e.getDayOfEvent().equals(d)) result.add(e);
        return result;
    }
}
