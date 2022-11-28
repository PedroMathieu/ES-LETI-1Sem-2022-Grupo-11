package pt.iscte.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.iscte.PersonalCalendar;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;


/**
 * Server service tests. This class does not contain
 * tests that rely on the server being alive. Since
 * the goal of these unit tests are to test logic and
 * basic functionality, some functions will not be tested.
 *
 * This test class needs some json calendars uploaded to
 * work. We might work on a script to develop a json
 * test calendar only when this test class runs.
 *
 * @author Jose Soares
 */
public class ServerServiceTest {
    private final Map<String, PersonalCalendar> testCalendars = ServerTestHelper.buildTestCalendars();
    private final LocalDate TEST_DATE = LocalDate.of(2022, 10, 27);
    private final String TEST_OWNER0 = "test0";
    private final String TEST_OWNER1 = "test1";


    @Test
    // This test works by checking if the owner name of an event is correct.
    public void givenCorrectOwnerAndDate_whenBuildingEvents_returnsCorrectJson() {
        JSONObject jsonEventsOwner0 = ServerService.buildEventsInJson(TEST_OWNER0, TEST_DATE, testCalendars);
        JSONObject jsonEventsOwner1 = ServerService.buildEventsInJson(TEST_OWNER1, TEST_DATE, testCalendars);

        // First event of each owner (gets in events array and gets first event)
        JSONObject eventsOfOwner0 = (JSONObject) ((JSONArray) jsonEventsOwner0.get("events")).get(0);
        JSONObject eventsOfOwner1 = (JSONObject) ((JSONArray) jsonEventsOwner1.get("events")).get(0);

        // Names in processed json
        String actualNameOfOwner0 = eventsOfOwner0.get("owner").toString();
        String actualNameOfOwner1 = eventsOfOwner1.get("owner").toString();

        Assertions.assertEquals(TEST_OWNER0, actualNameOfOwner0);
        Assertions.assertEquals(TEST_OWNER1, actualNameOfOwner1);
    }

    @Test
    public void whenDeletingTempIcsFiles_allFilesAreDeleted() {
        File icsFolder = new File(System.getProperty("user.dir") + "/calendars/icsFiles/");
        ServerTestHelper.createDummyTempIcsFiles(icsFolder);

        // Makes sure dummy files were created
        Assertions.assertEquals(11, icsFolder.listFiles().length);

        // Make sure the dummy files were deleted
        ServerService.deleteTempIcsFiles();
        Assertions.assertEquals(0, icsFolder.listFiles().length);
    }
}
