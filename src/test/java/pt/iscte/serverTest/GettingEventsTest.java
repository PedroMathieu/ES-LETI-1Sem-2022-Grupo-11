package pt.iscte.serverTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GettingEventsTest {
    @Test
    @DisplayName("Test given date format for Integer conversion")
    public void testDateConversion() throws Exception {
        List<ApiTestUtils.TestResponse> tests = new ArrayList<>();
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/year/11/3"));
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/2022/november/3"));
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/2022/11/three"));

        for (ApiTestUtils.TestResponse test : tests) {
            assertTrue(test.body.contains("Year, month or day is not a number"));
        }
    }

    @Test
    @DisplayName("Test asking for an invalid calendar owner")
    public void testInvalidOwner() throws Exception {
        ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", "/personalCalendar/e/xx/2022/11/3");
        assertTrue(res.body.contains("Parameters contain problems"));
    }

    @Test
    @DisplayName("Test date validation")
    public void testDateValidation() throws Exception {
        //TODO: is there any way to make this more clean?

        List<ApiTestUtils.TestResponse> tests = new ArrayList<>();
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/3001/11/3"));
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/999/11/3"));
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/2023/13/3"));
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/2023/0/3"));
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/2023/1/32"));
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/2023/2/29"));
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/2023/4/31"));
        tests.add(ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/2023/4/0"));

        for (ApiTestUtils.TestResponse test : tests) {
            assertTrue(test.body.contains("Parameters contain problems"));
        }
    }

    @Test
    @DisplayName("Test leap years")
    public void testLeapYears() throws Exception {
        ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/2024/2/29");
        assertTrue(!res.body.contains("Something wrong in parameters"));
    }

    @Test
    @DisplayName("Test if no errors are returned when each parameter is correct")
    public void testpersonalCalendarByDayWorksCorrectly() throws Exception {
        ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", "/personalCalendar/e/jphjs/2022/11/4");
        assertTrue(!res.body.contains("Something wrong in parameters"));
        assertTrue(!res.body.contains("Year, month or day is not a number"));
        assertTrue(!res.body.contains("Something wrong in parameters"));
        assertEquals(200, res.status);
    }
}
