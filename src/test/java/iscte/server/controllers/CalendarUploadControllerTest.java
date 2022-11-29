package iscte.server.controllers;

import iscte.server.controllers.CalendarUploadController;
import iscte.server.controllers.Controller;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import iscte.server.ServerService;
import pt.iscte.TestUtils;

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
                cu.process(TestUtils.buildUploadCalendarParams("https://www.google.com"));
        String errorMessage = (String) response.get("error");
        Assertions.assertTrue(errorMessage.contains("make sure the url"));
        Assertions.assertEquals(true, (boolean) response.get("gotError"));
    }

    @Test
    public void givenNonExistingUrl_whenUploadingCalendar_returnsError() {
        Map<String, Object> response =
                cu.process(TestUtils.buildUploadCalendarParams("webcal://foobar"));
        String errorMessage = (String) response.get("error");
        Assertions.assertTrue(errorMessage.contains("calendar data"));
        Assertions.assertEquals(true, (boolean) response.get("gotError"));
    }

    @Test
    public void givenUrlThatsNotACalendar_whenUploadingCalendar_returnsError() {
        Map<String, Object> response =
                cu.process(TestUtils.buildUploadCalendarParams("webcal://www.google.com"));
        String errorMessage = (String) response.get("error");
        Assertions.assertTrue(errorMessage.contains("Parser got an error"));
        Assertions.assertEquals(true, (boolean) response.get("gotError"));
    }
}
