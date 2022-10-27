package pt.iscte;

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

	@Override
	public String toString() {
		return "Owner " + eventOwner 
			+ "\nSummary: " + eventSummary
			+ "\nStartDate: " + eventDateStart.toString()
			+ "\nEndDate: " + eventDateEnd.toString();
	}
}
