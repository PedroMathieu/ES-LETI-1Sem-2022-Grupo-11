package pt.iscte;

/**
 * Represents a full date, including day and times
 * that will be used by the events
 * 
 * @author Jose Soares 
 */
public class Date {
	private Day day;
	private Time time;

	/**
	 * Represents a day by its full representation year/month/day
	 * is part of date
     * 
     * @author Jose Soares
	 */
	public class Day {
		private int year;
		private int month;
		private int day;

        public Day(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

        /**
         * Day constructor using a string
         * @param day format must be: year/month/day
         */
		public Day(String day) {
			String[] daySplit = day.split("/");
            
            if (daySplit.length != 3) 
                throw new IllegalArgumentException("Check if the day format is the correct to use the String constructor");

			this.year = Integer.parseInt(daySplit[0]);
			this.month = Integer.parseInt(daySplit[1]);
			this.day = Integer.parseInt(daySplit[2]);
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

        // TODO: This should be an @Override of equals().
        // But for now, that's not working, will work on that later
        public boolean isTheSameDay(Day d) {
            if ((this.getYear() == d.getYear()) && (this.getMonth() == d.getMonth()) && (this.getDay() == d.getDay()))
                return true;   
            return false;
        }

		@Override
		public String toString() {
			return year + "/" + month + "/" + day;
		}
		
	}

    /**
     * Represents the time, including hours and minutes
     * is part of date
     * 
     * @author Jose Soares
     */
	public class Time {
        int hour;
        int minutes;

        public Time(int hour, int minutes) {
            this.hour = hour;
            this.minutes = minutes;
        }

        /**
         * Time constructor using a string
         * @param time format must be: hour:minutes
         */
        public Time(String time) {
            String[] timeSplit = time.split(":");
            if (timeSplit.length != 2) 
                throw new IllegalArgumentException("Check if the time format is the correct to use the String constructor");

			this.hour = Integer.parseInt(timeSplit[0]);
			this.minutes = Integer.parseInt(timeSplit[1]);
        }

        public int getHour() {
            return hour;
        }

        public int getMinutes() {
            return minutes;
        }

        @Override
        public String toString() {
            return hour + ":" + minutes;
        }
	}

	public Date(String dateString) {
		if (dateString.length() != 16) 
			throw new IllegalArgumentException("Date string is wrong size");
		
            else {
            this.day = new Day(Integer.parseInt(dateString.substring(0,4)),
                                Integer.parseInt(dateString.substring(4,6)),
                                Integer.parseInt(dateString.substring(6,8)));

            this.time = new Time(Integer.parseInt(dateString.substring(9,11)), 
                                Integer.parseInt(dateString.substring(11,13)));
		}
	}

	public Day getDay() {
		return day;
	}

	public Time getTime() {
		return time;
	}

	@Override
	public String toString() {
		return "Date: " + day.toString() + "@" + time.toString();
	}
}
