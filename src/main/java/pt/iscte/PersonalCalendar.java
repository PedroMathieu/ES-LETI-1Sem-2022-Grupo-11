package pt.iscte;

import java.io.FileReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 * This class represents a "virtual" personal calendar. It links
 * all the events from a parsed calendar to an owner.
 *
 * @author Jose Soares
 */
public class PersonalCalendar {
    String calendarFile = "";
    String calendarOwner = "";
    List<Event> events = new LinkedList<>();
    JSONParser parser = new JSONParser();
    public static LocalTime MORNING_START = LocalTime.of(5, 50);
    public static LocalTime MORNING_END = LocalTime.of(12, 29);
    public static LocalTime AFTERNOON_START = LocalTime.of(12, 29);
    public static LocalTime AFTERNOON_END = LocalTime.of(20, 0);


    public PersonalCalendar(String calendarFile) {
        this.calendarFile = calendarFile;
        parseJsonCalendar();
    }

    public PersonalCalendar() {}

    /**
     * Gets the calendar owner
     *
     * @return username of email of calendar owner (its unique)
     */
    public String getCalendarOwner() {
        return calendarOwner;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
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
                vCalendarObjects.add(vCalendarIterator.next());

            // From vcalendar, get the calendar owner (email) and the events
            calendarOwner = ((String) vCalendarObjects.get(0).get("x-wr-calname")).split("@")[0];
            JSONArray vEvents = (JSONArray) vCalendarObjects.get(0).get("vevent");
            Iterator<JSONObject> vEventsIterator = vEvents.iterator();

            // Add all the events to a JSONObject arraylist
            while (vEventsIterator.hasNext())
                eventListJson.add(vEventsIterator.next());

            // Convert all events from JSON to Event
            setEvents(convertEventListFromJSON(eventListJson));

        } catch (Exception e) {
            System.err.println("Couldn't read file " + calendarFile);
        }
    }

    /**
     * Converts all the events in JSON to Event objects
     * Makes it easier for searching
     * 
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
                    (String) j.get("dtend")));
        }

        return eventsToReturn;
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
            if (e.getDayOfEvent().equals(d))
                result.add(e);
        return result;
    }

    /**
     * Gets all the events between two dates
     *
     * @param startDate date to start search
     * @param endDate date to end search
     * @return list of events between the two dates
     */
    public List<Event> getEventsBetweenTwoDates(LocalDate startDate, LocalDate endDate, String timeOfDay) {
        List<Event> result = new ArrayList<>();

        for (Event e : events) {
            // Get events between dates
            if ((e.getEventDateStart().isAfter(startDate) && e.getEventDateEnd().isBefore(endDate)) ||
                (e.getEventDateStart().equals(startDate) || e.getEventDateEnd().equals(endDate))) {

                // If there's an event starting at 12:29, it might cause a bug
                // no need to worry atm
                if (e.getEventTimeStart().isAfter(MORNING_START) &&
                    e.getEventTimeStart().isBefore(MORNING_END) &&
                    timeOfDay.equals("Morning")) {
                        result.add(e);

                } else if (e.getEventTimeStart().isAfter(AFTERNOON_START) &&
                    e.getEventTimeStart().isBefore(AFTERNOON_END) &&
                    timeOfDay.equals("Afternoon")) {
                        result.add(e);
                }
            }
        }

        return result;
    }
}
