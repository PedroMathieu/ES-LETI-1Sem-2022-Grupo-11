package pt.iscte.server.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.iscte.server.ServerService;
import pt.iscte.server.ServerTestHelper;

import java.util.Map;

public class CalendarUploadControllerTest {
    private Controller cu = new CalendarUploadController();

    @AfterEach
    public void deleteTempFiles() {
        ServerService.deleteTempIcsFiles();
    }

    @Test
    public void givenWrongProtocol_whenUploadingCalendar_returnsError() {
        Map<String, Object> response =
                cu.process(ServerTestHelper.buildUploadCalendarParams("https://www.google.com"));
        String errorMessage = (String) response.get("error");
        Assertions.assertTrue(errorMessage.contains("make sure the url"));
        Assertions.assertEquals(true, (boolean) response.get("gotError"));
    }

    @Test
    public void givenNonExistingUrl_whenUploadingCalendar_returnsError() {
        Map<String, Object> response =
                cu.process(ServerTestHelper.buildUploadCalendarParams("webcal://foobar"));
        String errorMessage = (String) response.get("error");
        Assertions.assertTrue(errorMessage.contains("calendar data"));
        Assertions.assertEquals(true, (boolean) response.get("gotError"));
    }

    @Test
    public void givenUrlThatsNotACalendar_whenUploadingCalendar_returnsError() {
        Map<String, Object> response =
                cu.process(ServerTestHelper.buildUploadCalendarParams("webcal://www.google.com"));
        String errorMessage = (String) response.get("error");
        Assertions.assertTrue(errorMessage.contains("Parser got an error"));
        Assertions.assertEquals(true, (boolean) response.get("gotError"));
    }
}
