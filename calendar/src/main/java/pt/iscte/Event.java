package pt.iscte;

import java.time.LocalTime;
import java.util.Map;
import java.time.LocalDate;
import java.util.HashMap;

/**
 * Represents an event. It contains an owner, summary and start / end dates
 * 
 * @author Jose Soares
 */
public class Event {
	private String eventOwner;
	private String eventSummary;
	private LocalDate eventDateStart;
    private LocalTime eventTimeStart;

	private LocalDate eventDateEnd;
    private LocalTime eventTimeEnd;

	public Event(String eventOwner, String eventSummary, String eventDateStart, String eventDateEnd) {
		this.eventOwner = eventOwner;
		this.eventSummary = eventSummary;

        int yearStart = Integer.parseInt(eventDateStart.substring(0,4));
        int monthStart = Integer.parseInt(eventDateStart.substring(4,6));
        int dayStart = Integer.parseInt(eventDateStart.substring(6,8));
        int hourStart = Integer.parseInt(eventDateStart.substring(9,11));
        int minutesStart = Integer.parseInt(eventDateStart.substring(11,13));
        this.eventDateStart = LocalDate.of(yearStart, monthStart, dayStart);
        this.eventTimeStart = LocalTime.of(hourStart, minutesStart);

        int yearEnd = Integer.parseInt(eventDateEnd.substring(0,4));
        int monthEnd = Integer.parseInt(eventDateEnd.substring(4,6));
        int dayEnd = Integer.parseInt(eventDateEnd.substring(6,8));
        int hourEnd = Integer.parseInt(eventDateEnd.substring(9,11));
        int minutesEnd = Integer.parseInt(eventDateEnd.substring(11,13));
        this.eventDateEnd = LocalDate.of(yearEnd, monthEnd, dayEnd);
        this.eventTimeEnd = LocalTime.of(hourEnd, minutesEnd);
	}


    /**
     * Get day of event. Only works if event starts and ends in
     * the same day.
     * @return date of the event
     */
    public LocalDate getDayOfEvent() {
        if (eventDateStart.equals(eventDateEnd)) return eventDateStart;
        return null;
    }


	@Override
	public String toString() {
		return "Owner " + eventOwner 
			+ "\nSummary: " + eventSummary
			+ "\nStartDate: " + eventDateStart
			+ "\nEndDate: " + eventDateEnd;
	}
}
