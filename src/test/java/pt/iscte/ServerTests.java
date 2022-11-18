package pt.iscte;

import org.junit.jupiter.api.*;
import spark.Spark;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerTests {
    private static Server s;

    @BeforeEach
    public void setUp() throws Exception {
        s = new Server();
        Spark.awaitInitialization();

    }
    @AfterEach
    public void tearDown() throws Exception {
        Spark.stop();
        Spark.awaitStop();
    }

    @Test
    @DisplayName("Calendars uploaded are at least 1")
    public void testCalendarMinimum() {
        assertTrue(s.getPersonalCalendarObjects().size() >= 1);
    }

    @Test
    @DisplayName("Test if server is alive")
    public void isAlive() throws Exception {
        ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", "/");
        assertEquals(200, res.status);
    }

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

    @Test
    @Tag("Calendar upload")
    @DisplayName("Test enviar link com protocolo errado")
    public void testWrongProtocolCalendarUpload() throws Exception {
        //TODO
    }

    @Test
    @Tag("Calendar upload")
    @DisplayName("Test assure temp file is always deleted")
    public void testTempFileIsDeletedCalendarUpload() {
        //TODO
    }

    @Test
    @Tag("Calendar upload")
    @DisplayName("Test send link that is not .ics")
    public void testLinkThatsNotIcsCalendarUpload() {
        //TODO
    }

    @Test
    @Tag("Calendar upload")
    @DisplayName("Test a link that does not exist")
    public void testLinkThatDoesNotExistCalendarUpload() {
        //TODO
    }

}
