package pt.iscte;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.event.SwingPropertyChangeSupport;

import java.time.LocalDate;

/**
 * This class' job will be to take the information stored in a .ics file
 * stored in /calendar/icsCalendars and convert its information to a
 * .json file where the other functions developed in other classes can
 * be applied.
 * 
 * @author Joao Marques
 */
public class Parser {

    // Calendar information
    private String calendar_prodID;
    private double calendar_version;
    private String calendar_type;
    private String calendar_name;

    // Flags
    private boolean flag_CanReadFile;

    /**
     * The starting function that takes the name of the file to
     * be scanned and starts reading parts of it, redirecting
     * to other functions to store the necessary information for
     * the output of the .json file.
     * 
     * @param filename - the name of the .ics file to be scanned
     */
    public void initiateCalendar(String filename) {
        File file = new File(System.getProperty("user.dir") + "/icsCalendars/" + filename);

        try {

            // starts scanning the file
            Scanner s = new Scanner(file);
            while (s.hasNextLine() && flag_CanReadFile != false) {

                // stores the information of the current line in a String
                String line = s.nextLine();

                if (line.equals("BEGIN:VCALENDAR"))
                    flag_CanReadFile = true;

                else if (line.equals("END:VCALENDAR"))
                    flag_CanReadFile = false;

                else if (flag_CanReadFile)
                    takeTheInfo(line);

            }
        } catch (FileNotFoundException e) { // in case something happens when it tries to read the file
            System.err.println("The file " + filename + "was not found/couldn't be opened properly!");
        }
    }

    /**
     * 
     * @param line - the information of the line sent from initiateCalendar()
     */
    public void takeTheInfo(String line) {
        if (line.startsWith("PRODID:"))
            calendar_prodID = line.replace("PRODID:", "");
        System.out.println(calendar_prodID);
        flag_CanReadFile = false;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String s = scan.nextLine();
        Parser p = new Parser();
        p.initiateCalendar(s);
    }
}
