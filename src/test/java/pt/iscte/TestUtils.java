package pt.iscte;

import pt.iscte.Event;
import pt.iscte.PersonalCalendar;
import iscte.server.ServerUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestUtils {
     /**
     * Builds dummy events to use for building test calendars
     *
     * @param calendarNumber number of calendar, from iteration in buildTestCalednars
     * @return list of events
     */
    public static List<Event> buildTestEvents(int calendarNumber) {
        List<Event> events = new ArrayList<>();
        events.add(new Event("test" + calendarNumber, "summary" + calendarNumber, "20221027T150000Z", "20221027T163000Z"));
        events.add(new Event("test" + calendarNumber, "summary" + calendarNumber, "20221027T150000Z", "20221027T163000Z"));
        return events;
    }

    /**
     * Builds dummy calendars to use for tests
     *
     * @return test calendars
     */
    public static Map<String, PersonalCalendar> buildTestCalendars() {
        Map<String, PersonalCalendar> testCalendar = new HashMap<>();

        for (int i = 0; i < 2; i++) {
            PersonalCalendar calendar = new PersonalCalendar();
            calendar.setEvents(buildTestEvents(i));
            testCalendar.put("test" + i, calendar);
        }

        return testCalendar;
    }

    /**
     * Creates dummy ics temp files
     */
    public static void createDummyTempIcsFiles(File icsFolder) {
        for (int i = 0; i <= 10; i++) {
            String tempFileName = ServerUtil.generateRandomTempName() + "_tempTest.ics";
            String pathOftemp = icsFolder.getAbsolutePath() + "/" + tempFileName;
            File f = new File(pathOftemp);

            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static List<String> getRandomNames() {
        List<String> randomNames = new ArrayList<>();

        for (int i = 0; i < 100; i++)
            randomNames.add(ServerUtil.generateRandomTempName());

        return randomNames;
    }

    public static Map<String, String> buildGetEventsParams(String owners, String operation, String year, String month, String day) {
        Map<String, String> params = new HashMap<>();
        params.put(":userid", owners);
        params.put(":operation", operation);
        params.put(":year", year);
        params.put(":month", month);
        params.put(":day", day);
        return params;
    }

    public static Map<String, String> buildUploadCalendarParams(String calendarUrl) {
        Map<String, String> params = new HashMap<>();
        params.put("calendarLink", calendarUrl);
        return params;
    }
}
