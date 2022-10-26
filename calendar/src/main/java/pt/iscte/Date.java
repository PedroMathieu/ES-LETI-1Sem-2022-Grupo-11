package pt.iscte;

public class Date {
	private int year;
	private int month;
	private int day;
	private int hour;
	private int minutes;

	public Date(String dateString) {
		if (dateString.length() != 16) 
			throw new IllegalArgumentException("Date string is wrong size");
		else {
			this.year = Integer.parseInt(dateString.substring(0,4));
			this.month = Integer.parseInt(dateString.substring(4,6));
			this.day = Integer.parseInt(dateString.substring(6,8));
			this.hour = Integer.parseInt(dateString.substring(9,11));
			this.minutes = Integer.parseInt(dateString.substring(11,13));
		}
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}
	
	public int getDay() {
		return day;
	}

	public int getHour() {
		return hour;
	}

	public int getMinutes() {
		return minutes;
	}

	public String toString() {
		return "Date: " + year + "/" + month + "/" + day + "@" + hour + ":" + minutes;
	}
}
