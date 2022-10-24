package pt.iscte;

import java.io.FileReader;
import java.io.Reader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Calendar {
    String id = "";
    String calendarFile = "";
    JSONParser parser = new JSONParser();

    public Calendar(String id, String calendarFile) {
        this.id = id;
        this.calendarFile = calendarFile;
        parseJsonCalendar();
    }

    public String getId() {
        return id;
    }

    public String getCalendarFile() {
        return calendarFile;
    }

    private void parseJsonCalendar() {
        try (Reader reader = new FileReader(calendarFile)) {

            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            System.out.println(jsonObject);

        } catch (Exception e) {
            System.err.println("Couldn't read file " + calendarFile);
        }
    }
}
