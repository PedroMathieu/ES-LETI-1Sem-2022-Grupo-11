package pt.iscte;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest{

    @Test
    public void returnsCorrectDTStampWhenReading(){
        String line = "DTSTAMP:20221215T022759Z";
        Parser parser = new Parser();
        parser.takeEventInfo(line);
        Assertions.assertEquals(parser.getEventCreationDate(), "20221215T022759Z");
    }

    @Test
    public void returnsCorrectDTStartWhenReading(){
        String line = "DTSTART:20221207T143000Z";
        Parser parser = new Parser();
        parser.takeEventInfo(line);
        Assertions.assertEquals(parser.getEventStartDate(), "20221207T143000Z");
    }

    @Test
    public void returnsCorrectDTEndWhenReading(){
        String line = "DTEND:20221207T160000Z";
        Parser parser = new Parser();
        parser.takeEventInfo(line);
        Assertions.assertEquals(parser.getEventEndDate(), "20221207T160000Z");
    }

    @Test
    public void returnsCorrectSummaryWhenReading(){
        String line = "SUMMARY:Modulação e Codificação - Modulation and Coding";
        Parser parser = new Parser();
        parser.takeEventInfo(line);
        Assertions.assertEquals(parser.getEventSummary(), "Modulação e Codificação - Modulation and Coding");
    }

    @Test
    public void returnsCorrectProductIDWhenReading(){
        String line = "PRODID:-//ISCTE-IUL//fenix//EN";
        Parser parser = new Parser();
        parser.takeTheCalendarInfo(line);
        Assertions.assertEquals(parser.getCalendarProductID(), "-//ISCTE-IUL//fenix//EN");
    }

    @Test
    public void returnsCorrectVersionWhenReading(){
        String line = "VERSION:2.0";
        Parser parser = new Parser();
        parser.takeTheCalendarInfo(line);
        Assertions.assertEquals(parser.getCalendarVersion(), "2.0");
    }

    @Test
    public void returnsCorrectCalendarTypeWhenReading(){
        String line = "CALSCALE:GREGORIAN";
        Parser parser = new Parser();
        parser.takeTheCalendarInfo(line);
        Assertions.assertEquals(parser.getCalendarType(), "GREGORIAN");
    }

    @Test
    public void returnsCorrectCalendarNameWhenReading(){
        String line = "X-WR-CALNAME:jlpms1@iscte.pt_iscte-iul";
        Parser parser = new Parser();
        parser.takeTheCalendarInfo(line);
        Assertions.assertEquals(parser.getCalendarName(), "jlpms1@iscte.pt_iscte-iul");
    }

}