package pt.iscte.serverTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class CalendarUploadTest {
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
