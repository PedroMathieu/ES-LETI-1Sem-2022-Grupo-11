package pt.iscte;

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
        Assertions.assertEquals(1, pc.getEventsInADay(LocalDate.of(2022, 9, 21)).size());
    }

    @Test
    public void givenListOfEvents_whenSettingEvents_thenCorrectlySetsNewEvents() {
        pc.setEvents(TestUtils.buildTestEvents(1));
        Assertions.assertEquals(2, pc.getEventsInADay(LocalDate.of(2022,10,27)).size());
    }
}
