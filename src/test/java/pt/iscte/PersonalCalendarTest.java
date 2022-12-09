package pt.iscte;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class PersonalCalendarTest {
    private PersonalCalendar pc = new PersonalCalendar(System.getProperty("user.dir") + "/src/test/testFiles/jsonCalendarTest.json");

    @Test
    public void givenCorrectJsonCalendarPath_returnsCorrectOwner() {
        Assertions.assertEquals("testCalendar", pc.getCalendarOwner());
    }

    @Test
    public void givenCorrectJsonCalendarPath_returnsCorrectEventsInADay() {
        Assertions.assertEquals(2, pc.getEventsInADay(LocalDate.of(2022, 9, 21)).size());
    }

    @Test
    public void givenListOfEvents_whenSettingEvents_thenCorrectlySetsNewEvents() {
        pc.setEvents(TestUtils.buildTestEvents(1));
        Assertions.assertEquals(2, pc.getEventsInADay(LocalDate.of(2022,10,27)).size());
    }

    @Test
    public void givenMorningFilter_whenGettingEvents_returnsCorrectEvents() {
        Assertions.assertEquals(2,
                pc.getEventsBetweenTwoDates(LocalDate.of(2022,9,21),
                        LocalDate.of(2022,9,21), "Morning").size());
    }

    @Test
    public void givenAfternoonFilter_whenGettingEvents_returnsCorrectEvents() {
         Assertions.assertEquals(1,
                pc.getEventsBetweenTwoDates(LocalDate.of(2022,9,22),
                        LocalDate.of(2022,9,22), "Afternoon").size());
    }

    @Test
    public void givenDateInterval_whenGettingEvents_returnsCorrectEvents() {
        pc.setEvents(TestUtils.buildTestEvents(1));
        Assertions.assertEquals(2,
                pc.getEventsBetweenTwoDates(LocalDate.of(2022,10,27),
                        LocalDate.of(2022,10,27), "Afternoon").size());
    }

    @Test
    public void givenEventStartingInFirstMorningHour_whenGettingEventsInSameHour_returnsEvents() {
        Assertions.assertEquals(2,
            pc.getEventsBetweenTwoDates(LocalDate.of(2022,9,21),
            LocalDate.of(2022,9,21), "Morning").size());
    }

    @Test
    public void givenEventStartingInConflictHour_whenGettingEvents_eventBelongsToAfternoon() {
        Assertions.assertEquals(1,
            pc.getEventsBetweenTwoDates(LocalDate.of(2022,9,1),
                LocalDate.of(2022,9,1), "Afternoon").size());
    }
}
