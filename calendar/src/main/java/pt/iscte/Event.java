package pt.iscte;

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

        this.eventDateStart = LocalDate.of(
                Integer.parseInt(eventDateStart.substring(0,4)),
                Integer.parseInt(eventDateStart.substring(4,6)),
                Integer.parseInt(eventDateStart.substring(6,8)));
        this.eventTimeStart = LocalTime.of(Integer.parseInt(eventDateStart.substring(9,11)),
                Integer.parseInt(eventDateStart.substring(11,13)));

        this.eventDateEnd = LocalDate.of(
                Integer.parseInt(eventDateEnd.substring(0,4)),
                Integer.parseInt(eventDateEnd.substring(4,6)),
                Integer.parseInt(eventDateEnd.substring(6,8)));
        this.eventTimeEnd = LocalTime.of(Integer.parseInt(eventDateEnd.substring(9,11)),
                Integer.parseInt(eventDateEnd.substring(11,13)));
	}


    /**
     * Get day of event. Only works if event starts and ends in
     * the same day.
     * @return date of the event, null if event happens in more than one day
     */
    public LocalDate getDayOfEvent() {
        if (eventDateStart.equals(eventDateEnd)) return eventDateStart;
        return null;
    }

    /**
     * Get date when event starts
     * @return date of event start
     */
    public LocalDate getEventDateStart() {
        return eventDateStart;
    }

    /**
     * Get date when event starts
     * @return date of event end
     */
    public LocalDate getEventDateEnd() {
        return eventDateEnd;
    }

    /**
     * Get time when event starts
     * @return time the event starts
     */
    public LocalTime getEventTimeStart() {
        return eventTimeStart;
    }

    /**
     * Get time when event ends
     * @return time the event ends
     */
    public LocalTime getEventTimeEnd() {
        return eventTimeEnd;
    }


	@Override
	public String toString() {
		return "Owner: " + eventOwner
			+ "\nSummary: " + eventSummary
			+ "\nStartDate: " + eventDateStart
			+ "\nEndDate: " + eventDateEnd;
	}
}
