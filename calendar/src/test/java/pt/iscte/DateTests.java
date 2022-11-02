package pt.iscte;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Class that tests Date and its inner classes (Day and Time)
 *
 * @author Jose Soares
 */
class DateTests {
    private final Date dWithString = new Date("20220928T133000Z");

    @Test
    @DisplayName("Does not allow Date creation with wrong length")
    void dateDoesNotAllowWrongLength() {
    }

    @Test
    @DisplayName("20220928T133000Z => returns 2022/09/28")
    void returnsDayCorrectly() {
        assertEquals(dWithString.getTime().toString(), "2022/09/28");
    }

    @Test
    @DisplayName("20220928T133000Z => returns 13:30")
    void returnsTimeCorrectly() {
        assertEquals(dWithString.getTime().toString(), "13:30");
    }

    @Test
    @DisplayName("Does not allow Time object creation with wrong length")
    void timeDoesNotAllowWrongLength() {}

    @Test
    @DisplayName("Does not allow Time object creation when hour or minutes length is wrong")
    void timeDoesNotAllowWrongSplit() {}

    @Test
    @DisplayName("13:30 => returns 13")
    void getHoursOfTime() {}

    @Test
    @DisplayName("13:30 => returns 30")
    void getMinutesOfTime() {

    }

    // TODO: test Day class and build more tests for the whole Date class
}
