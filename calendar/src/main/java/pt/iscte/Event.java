package pt.iscte;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.time.LocalTime;
import java.time.LocalDate;

/**
 * Represents an event. It contains an owner, summary and start / end dates
 * 
 * @author Jose Soares
 */
public class Event {
    private final String eventOwner;
    private final String eventSummary;
    private final LocalDate eventDateStart;
    private final LocalTime eventTimeStart;
    private final LocalDate eventDateEnd;
    private final LocalTime eventTimeEnd;

    public Event(String eventOwner, String eventSummary, String eventDateStart, String eventDateEnd) {
        this.eventOwner = eventOwner;
        this.eventSummary = eventSummary;

        // Sets the start date from the string obtained in the JSON file
        this.eventDateStart = LocalDate.of(
                getLocalYear(eventDateStart),
                getLocalMonth(eventDateStart),
                getLocalDay(eventDateStart));

        // Sets the start time of the event obtained from the JSON file
        this.eventTimeStart = LocalTime.of(getLocalHour(eventDateStart),
                getLocalMinutes(eventDateStart));

        // Sets the end date of the event obtained from the JSON file
        this.eventDateEnd = LocalDate.of(
                getLocalYear(eventDateEnd), 
                getLocalMonth(eventDateEnd),
                getLocalDay(eventDateEnd));

        // Sets the end time of the event obtained from the JSON file
        this.eventTimeEnd = LocalTime.of(getLocalHour(eventDateEnd),
                getLocalMinutes(eventDateEnd));
    }

    /**
     * 
     * @param s String that an int will be parsed from
     * @param x First index of the string from which the int will be parsed
     * @param y Index after end of said string
     * @return parsed int from s
     */
    public static int parse(String s, int x, int y) {
        return Integer.parseInt(s.substring(x, y));
    }


    

    /**
     * The following functions serve to streamline the code.
     * Each of them obtains components of the date/hour from
     * a string inserted into the functions.
     * 
     * The constants used in the substrings are the expected
     * values of the indexes for the start and end of each 
     * component.
     */

    public static int getLocalYear(String s) {
        return parse(s, 0, 4);
    }

    public static int getLocalMonth(String s) {
        return parse(s, 4, 6);
    }

    public static int getLocalDay(String s) {
        return parse(s, 6, 8);
    }

    public static int getLocalHour(String s) {
        return parse(s, 9, 11);
    }

    public static int getLocalMinutes(String s) {
        return parse(s, 11, 13);
    }




    /**
     * Get day of event. Only works if event starts and ends in
     * the same day.
     * 
     * @return date of the event, null if event happens in more than one day
     */
    public LocalDate getDayOfEvent() {
        if (eventDateStart.equals(eventDateEnd))
            return eventDateStart;
        return null;
    }

    /**
     * Get date when event starts
     * 
     * @return date of event start
     */
    public LocalDate getEventDateStart() {
        return eventDateStart;
    }

    /**
     * Get date when event starts
     * 
     * @return date of event end
     */
    public LocalDate getEventDateEnd() {
        return eventDateEnd;
    }

    /**
     * Get time when event starts
     * 
     * @return time the event starts
     */
    public LocalTime getEventTimeStart() {
        return eventTimeStart;
    }

    /**
     * Get time when event ends
     * 
     * @return time the event ends
     */
    public LocalTime getEventTimeEnd() {
        return eventTimeEnd;
    }

    /**
     * Builds the JSON object of the current event
     *
     * @return json object of current event
     */
    public JSONObject convertEventToJson() {
        JSONObject json = new JSONObject();
        json.put("owner", eventOwner);
        json.put("summary", eventSummary);
        json.put("date_start", eventDateStart.toString());
        json.put("time_start", eventTimeStart.toString());
        json.put("date_end", eventDateEnd.toString());
        json.put("time_end", eventTimeEnd.toString());
        return json;
    }

    @Override
    public String toString() {
        return "Owner: " + eventOwner
                + "\nSummary: " + eventSummary
                + "\nStartDate: " + eventDateStart
                + "\nEndDate: " + eventDateEnd;
    }
}
