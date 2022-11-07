package pt.iscte;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * This class' job will be to take the information stored in a .ics file stored
 * in /calendar/icsCalendars and convert its information to a .json file where
 * the other functions developed in other classes can be applied.
 * 
 * @author Joao Marques
 */
public class Parser {

    // Directory
    public final String DIR = System.getProperty("user.dir") + "/calendars/";

    // Path for the output .json file
    private Path path;

    // Calendar information
    private String calendar_prodID;
    private String calendar_version;
    private String calendar_type;
    private String calendar_name;

    // Event information
    private String event_creationDate;
    private String event_startDate;
    private String event_endDate;
    private String event_Summary;
    private String event_uID;
    private String event_Description;
    private String event_Location;

    // Flags
    private boolean flag_CanReadFile = false;
    private boolean flag_TakenCalendarInfo = false;
    private boolean flag_ReadingDescription = false;
    private boolean flag_FirstEvent = true;

    /**
     * The starting function that takes the name of the file to be scanned and
     * starts reading parts of it, redirecting to other functions to store the
     * necessary information for the output of the .json file.
     * 
     * @param filename - the name of the .ics file to be scanned
     */
    public void initiateCalendar(String filename) throws IOException {
        File file = new File(DIR + filename);
        System.out.println(System.getProperty("user.dir"));

        try {

            // starts scanning the file
            Scanner s = new Scanner(file);
            String line;

            // scans the file once and continues if it found the BEGIN tag
            do {

                // stores the information of the current line in a String
                line = s.nextLine();
                line = line.replace("\\", "\\\\");

                // depending on the tag of the line taken, it might trigger different flags
                switch (line) {

                    // if it managed o find the BEGIN tag, it will be allowed to keep
                    // reading the file
                    case ("BEGIN:VCALENDAR"):
                        flag_CanReadFile = true;
                        break;

                    // if it found the END tag its job is done and won't keep reading the file
                    case ("END:VCALENDAR"):
                        flag_CanReadFile = false;
                        Files.writeString(path, Files.readString(path) + "\n" + generateEndTemplate());
                        break;

                    // having found a BEGINEVENT tag it means it has already previously taken in the
                    // calendar info and can now move on to reading event information
                    case ("BEGIN:VEVENT"):
                        if (!flag_TakenCalendarInfo) {
                            flag_TakenCalendarInfo = true;
                            writeMainCalendarInfo();
                        }
                        break;

                    // having found the ENDEVENT tag it has finished reading the current event, and
                    // it changes the flag that refers to being currently reading the description to
                    // false, as well as cleaning the local variables and writing the taken info
                    // into the .json file
                    case ("END:VEVENT"):
                        writeCurrentEventCalendarInfo();
                        cleanEventVariables();
                        flag_ReadingDescription = false;
                        break;

                    // not having found any of the special tags, it will read the file as normal
                    default:

                        // if it hasn't taken in the calendar info yet, it'll do so
                        if (!flag_TakenCalendarInfo) {
                            takeTheCalendarInfo(line);
                        }

                        // continues taking in the description until it reaches the next parameter
                        else if (flag_ReadingDescription && !line.startsWith("LOCATION:")) {
                            // erases the first character since it's a space that's not needed
                            event_Description = event_Description + line.substring(1, line.length());
                        }

                        // taking in the events of the calendar
                        else {
                            takeEventInfo(line);
                        }
                }

            } while (s.hasNextLine() && flag_CanReadFile);

            // in case it prematurely closes because it didn't find the beginning
            if (!line.equals("BEGIN:VCALENDAR") && !line.equals("END:VCALENDAR")) {
                System.err.println("ERROR IN LINE: \"" + line + "\"");
            }
            s.close();

        } catch (FileNotFoundException e) { // in case something happens when it tries to read the file
            System.err.println("ERROR: The file " + file + " was not found/couldn't be opened properly!");
        }
    }

    /**
     * Takes the main calendar information and stores it in local variables so that
     * they can be output into the .json file.
     * 
     * @param line - the information of the line sent from initiateCalendar()
     */
    public void takeTheCalendarInfo(String line) {

        // takes in the calendar prodID
        if (line.startsWith("PRODID:")) {
            calendar_prodID = line.replace("PRODID:", "");
        }

        // takes in the calendar version
        else if (line.startsWith("VERSION:")) {
            calendar_version = line.replace("VERSION:", "");
        }

        // takes in the calendar type (usually Gregorian)
        else if (line.startsWith("CALSCALE:")) {
            calendar_type = line.replace("CALSCALE:", "");
        }

        // takes in the name of the calendar
        else if (line.startsWith("X-WR-CALNAME:")) {
            calendar_name = line.replace("X-WR-CALNAME:", "");
            path = Path.of(DIR + "jsonFiles/" + calendar_name + ".json");
        }

    }

    /**
     * Takes the information of the event currently being read and stores it in the
     * local variables so that it can be written to the .json file.
     * 
     * This process will be done by event so that only 1 event has to be stored
     * locally. As the event is processed it'll be output into the .json file and
     * the local variables will be cleansed.
     * 
     * 
     * @param line - the content of the line taken from the Scanner in
     *             initiateCalendar()
     */
    public void takeEventInfo(String line) {

        // takes in the date of the creation of the .ics file
        if (line.startsWith("DTSTAMP:")) {
            event_creationDate = line.replace("DTSTAMP:", "");
        }

        // takes in the starting date of the event
        else if (line.startsWith("DTSTART:")) {
            event_startDate = line.replace("DTSTART:", "");
        }

        // takes in the ending date of the event
        else if (line.startsWith("DTEND:")) {
            event_endDate = line.replace("DTEND:", "");
        }

        // takes in the summary of the event
        else if (line.startsWith("SUMMARY:")) {
            event_Summary = line.replace("SUMMARY:", "");
        }

        else if (line.startsWith("UID:")) {
            event_uID = line.replace("UID:", "");
        }

        // takes in the first line of the description
        else if (line.startsWith("DESCRIPTION:")) {
            event_Description = line.replace("DESCRIPTION:", "");
            flag_ReadingDescription = true;
        }

        // takes in the location of the event
        else if (line.startsWith("LOCATION:")) {
            event_Location = line.replace("LOCATION:", "");
        }

    }

    /**
     * Used to encode the strings (which are originally encoded with UTF-16) taken
     * from the .ics file to UTF-8, their original enconding standard, to cleanly
     * print them later.
     * 
     * @param s - input string
     * @return same string encoded with UTF-8
     */
    public String encodeUTF8(String s) {
        return s = new String(s.getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * Takes the info stored locally and the template stored in the templates folder
     * and inserts in a new .json file, with the same name as the .ics, the main
     * calendar information obtained from said .ics in the correct fields.
     */
    public void writeMainCalendarInfo() throws IOException {
        String template = generateMainTemplate(); // takes the template file for reference

        // replaces in the template the right fields with the corresponding information
        // taken in beforehand
        template = template.replace("CAL_PRODID", calendar_prodID);
        template = template.replace("CAL_VERSION", calendar_version);
        template = template.replace("CAL_CALSCALE", calendar_type);
        template = template.replace("CAL_CALNAME", calendar_name);

        Files.writeString(path, encodeUTF8(template) + "\n");
    }

    /**
     * 
     */
    public void writeCurrentEventCalendarInfo() throws IOException {
        if (!flag_FirstEvent) {
            Files.writeString(path, Files.readString(path) + ",\n");
        } else {
            flag_FirstEvent = false;
        }

        String template = generateEventTemplate();

        template = template.replace("CAL_DTSTAMP", event_creationDate);
        template = template.replace("CAL_DTSTART", event_startDate);
        template = template.replace("CAL_DTEND", event_endDate);
        template = template.replace("CAL_SUMMARY", event_Summary);
        template = template.replace("CAL_UID", event_uID);
        template = template.replace("CAL_DESCRIPTION", event_Description);
        template = template.replace("CAL_LOCATION", event_Location);

        Files.writeString(path, Files.readString(path) + encodeUTF8(template));
    }

    /**
     * Just takes the template from the template file mainInfoTemplate.txt for it to
     * later have specific fields replaced with the correct information obtained
     * from the .ics file.
     * 
     * @return - a string with the template stored in mainInfoTemplate.txt
     * @throws IOException
     */
    public String generateMainTemplate() throws IOException {
        return Files.readString(Path.of(DIR + "/templates/mainInfoTemplate.txt"));
    }

    /**
     * Similar to generateMainTemplate() but with the template for the insertion of
     * events.
     * 
     * @see generateMainTemplate()
     * @return - a string with the template stored in eventInfoTemplate.txt
     * @throws IOException
     */
    public String generateEventTemplate() throws IOException {
        return Files.readString(Path.of(DIR + "/templates/eventInfoTemplate.txt"));
    }

    /**
     * @see generateMainTemplate() and @see generateEventTemplate()
     * @return - a string with the template stored in endingFileTemplate.txt
     * @throws IOException
     */
    public String generateEndTemplate() throws IOException {
        return Files.readString(Path.of(DIR + "/templates/endingFileTemplate.txt"));
    }

    /**
     * Cleans the variables relative to event info for the next event that might be
     * read from the .ics file
     */
    public void cleanEventVariables() {
        event_Description = "";
        event_Location = "";
        event_Summary = "";
        event_creationDate = "";
        event_endDate = "";
        event_startDate = "";
        event_uID = "";
    }

    public static void readFiles(){
        
    }   




    public static void main(String[] args) throws IOException {
        Parser p = new Parser();
        System.out.print("Enter your file's name: ");
        Scanner scan = new Scanner(System.in);
        String filename = scan.nextLine();
        scan.close();

        p.initiateCalendar(filename);
    }

    public void printCalendarInfo() {
        printCalendarMainInfo();
        printCalendarEventInfo();
    }

    public void printCalendarMainInfo() {
        System.out.println("\n\nCALENDAR NAME = " + encodeUTF8(calendar_name));
        System.out.println("CALENDAR PRODUCT ID = " + encodeUTF8(calendar_prodID));
        System.out.println("CALENDAR VERSION = " + encodeUTF8(calendar_version));
        System.out.println("CALENDAR TYPE = " + encodeUTF8(calendar_type));
    }

    public void printCalendarEventInfo() {
        System.out.println("\n\nEVENT STAMP = " + encodeUTF8(event_creationDate));
        System.out.println("EVENT START DATE = " + encodeUTF8(event_startDate));
        System.out.println("EVENT END DATE = " + encodeUTF8(event_endDate));
        System.out.println("EVENT SUMMARY = " + encodeUTF8(event_Summary));
        System.out.println("EVENT UID = " + encodeUTF8(event_uID));
        System.out.println("EVENT DESCRIPTION = " + encodeUTF8(event_Description));
        System.out.println("EVENT LOCATION = " + encodeUTF8(event_Location));
    }
}
