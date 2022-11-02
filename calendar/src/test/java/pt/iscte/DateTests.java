package pt.iscte;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Class that tests Date and its inner classes (Day and Time)
 *
 * @author Jose Soares
 */
class DateTests {
    private final Date dWithString = new Date("20220928T133000Z");
    private Date dWithWrongLength = null;
    private Date.Time timeHandler = null;
    private Date.Day dWithWrongSplit = null;
    private Date.Day dayHandler = null;

    @Test
    @Tag("Date")
    @DisplayName("Does not allow Date creation with wrong length")
    void dateDoesNotAllowWrongLength() {
        assertThrows(IllegalArgumentException.class,() -> {
            dWithWrongLength = new Date("20220928T1x33000Z");
        });
    }

    @Test
    @Tag("Date")
    @DisplayName("get day of 20220928T133000Z => returns 2022/09/28")
    void returnsDayCorrectly() {
        assertEquals(dWithString.getDay().toString(), "2022/9/28");
    }

    @Test
    @Tag("Date")
    @DisplayName("get time of 20220928T133000Z => returns 13:30")
    void returnsTimeCorrectly() {
        assertEquals(dWithString.getTime().toString(), "13:30");
    }

    @Test
    @Tag("Time")
    @DisplayName("Does not allow Time object creation with wrong split")
    void timeDoesNotAllowWrongSplit() {
        assertThrows(IllegalArgumentException.class,() -> {
            timeHandler = dWithString.new Time("12:30:1");
        });
    }

    @Test
    @Tag("Time")
    @DisplayName("Hour between 00 and 24")
    void hourBetweenCorrectRange() {
        assertThrows(ArithmeticException.class,() -> {
            timeHandler = dWithString.new Time("24:00");
        });
        assertDoesNotThrow(() -> {
            timeHandler = dWithString.new Time("00:00");
        });
    }

    @Test
    @Tag("Time")
    @DisplayName("Minutes between 00 and 60")
    void minutesBetweenCorrectRange() {
        assertThrows(ArithmeticException.class,() -> {
            timeHandler = dWithString.new Time("12:60");
        });
        assertDoesNotThrow(() -> {
            timeHandler = dWithString.new Time("12:00");
        });
    }

    @Test
    @Tag("Day")
    @DisplayName("Does not allow Day object creation with wrong split")
    void dateDoesNotAllowWrongSplit() {
        assertThrows(IllegalArgumentException.class,() -> {
            dayHandler = dWithString.new Day("2022/12/31/1");
        });
    }

    @Test
    @Tag("Day")
    @DisplayName("Days between 1 and 31")
    void daysBetweenCorrectRange() {
        // Even though some months don't have 31 days, this is just the general case
        assertThrows(ArithmeticException.class,() -> {
            dayHandler = dWithString.new Day("2025/12/00");
        });
        assertDoesNotThrow(() -> {
            dayHandler = dWithString.new Day("2025/12/01");
        });
    }

    @Test
    @Tag("Day")
    @DisplayName("Months between 1 and 12")
    void monthsBetweenCorrectRange() {
        // Even though some months don't have 31 days, this is just the general case
        assertThrows(ArithmeticException.class,() -> {
            dayHandler = dWithString.new Day("2025/00/01");
        });
        assertDoesNotThrow(() -> {
            dayHandler = dWithString.new Day("2025/1/01");
        });
    }

    @Test
    @Tag("Day")
    @DisplayName("Compare if two days are the same")
    void twoDaysAreSameDay() {
        Date.Day d1 = dWithString.new Day("2025/01/01");
        Date.Day d2 = dWithString.new Day("2023/01/01");

        // Test with different year
        assertEquals(d1.isTheSameDay(d2), false);

        // Test with different month
        d2 = dWithString.new Day("2025/02/01");
        assertEquals(d1.isTheSameDay(d2), false);

        // Test same day
        d2 = dWithString.new Day("2025/01/01");
        assertEquals(d1.isTheSameDay(d2), true);
    }
}
