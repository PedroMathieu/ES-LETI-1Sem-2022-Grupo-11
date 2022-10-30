package pt.iscte;

import pt.iscte.Date.Day;
import pt.iscte.Date.Time;

/**
 * Represents an event. It contains an owner, summary and start / end dates
 * 
 * @author Jose Soares
 */
public class Event {
	private String eventOwner;
	private String eventSummary;
	private Date eventDateStart;
	private Date eventDateEnd;

	public Event(String eventOwner, String eventSummary, String eventDateStart, String eventDateEnd) {
		this.eventOwner = eventOwner;
		this.eventSummary = eventSummary;
		this.eventDateStart = new Date(eventDateStart);
		this.eventDateEnd = new Date(eventDateEnd);
	}

    public Time getTimeOfEventStart() {
        return eventDateStart.getTime();
    }

    public Time getTimeOfEventEnd() {
        return eventDateEnd.getTime();
    }

    /**
     * Gets the day the event takes place
     * Assumes event starts and ends in the same day
     * 
     * @return corresponding day
     */
    public Day getDayOfEvent() {
        if (endsInTheSameDay())
            return eventDateEnd.getDay();
        return null;
    }

    /**
     * Checks if an event starts and ends in the same day.
     * We assume all the events in the calendars take place
     * in the same working day. If there's an event that
     * ends the day after it started it might cause bugs!
     * 
     * @return true if the event takes place in one day
     */
    private boolean endsInTheSameDay() {
        if (eventDateStart.getDay().isTheSameDay(eventDateEnd.getDay()))
            return true;
        return false;
    }

	@Override
	public String toString() {
		return "Owner " + eventOwner 
			+ "\nSummary: " + eventSummary
			+ "\nStartDate: " + eventDateStart.toString()
			+ "\nEndDate: " + eventDateEnd.toString();
	}
}
