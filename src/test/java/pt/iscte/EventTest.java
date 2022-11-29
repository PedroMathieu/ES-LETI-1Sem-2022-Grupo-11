package pt.iscte;

import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.iscte.Event;

import java.time.LocalDate;
import java.time.LocalTime;

public class EventTest {
    private Event e = new Event("test", "test", "20221213T160000Z", "20221213T160000Z");

    @Test
    public void givenIcsDates_whenParsing_returnsCorrectDates() {
        Assertions.assertEquals(e.getEventDateStart(), LocalDate.of(2022, 12, 13));
        Assertions.assertEquals(e.getEventDateEnd(), LocalDate.of(2022, 12, 13));
    }

    @Test
    public void givenIcsDates_whenParsing_returnsCorrectTimes() {
        Assertions.assertEquals(e.getEventTimeStart(), LocalTime.of(16, 0));
        Assertions.assertEquals(e.getEventTimeEnd(), LocalTime.of(16,0));
    }

    @Test
    public void givenEventThatEndsInSameDay_whenGettingDayOfEvent_returnsDate() {
        Assertions.assertEquals(e.getDayOfEvent(), LocalDate.of(2022, 12, 13));
    }

    @Test
    public void givenEventThatEndsOnDiffDay_whenGettingDayOfEvent_returnsNull() {
        Event endsOnDiffDay = new Event("test", "test", "20221213T160000Z", "20221214T160000Z");
        Assertions.assertEquals(null, endsOnDiffDay.getDayOfEvent());
    }

    @Test
    public void whenConvertingToJson_returnsCorrectJson() {
        JSONObject js = e.convertEventToJson();
        Assertions.assertEquals(js.get("owner"), "test");
        Assertions.assertEquals(js.get("summary"), "test");
        Assertions.assertEquals(js.get("date_start"), e.getEventDateStart().toString());
        Assertions.assertEquals(js.get("date_end"), e.getEventDateEnd().toString());
        Assertions.assertEquals(js.get("time_end"), e.getEventTimeStart().toString());
        Assertions.assertEquals(js.get("time_end"), e.getEventTimeEnd().toString());
    }
}
