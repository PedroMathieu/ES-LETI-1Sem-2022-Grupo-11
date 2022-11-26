package pt.iscte.server.controllers;

import pt.iscte.Parser;
import pt.iscte.server.ServerService;
import pt.iscte.server.ServerUtil;
import spark.Request;
import spark.Response;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Upload calendar controller. This controller is responsible for
 * uploading a given calendar link to the server and store it in
 * a folder.
 *
 * @author Jose Soares
 */
public class CalendarUploadController extends Controller {
    public CalendarUploadController() {}

    @Override
    public Object handle(Request req, Response res) {
        // Parse params to process from req.queryParams
        Map<String, String> paramsToProcess = new HashMap<>();
        paramsToProcess.put("calendarLink", req.queryParams("calendar-link-input"));

        Map<String, Object> processingReturns = process(paramsToProcess);

        if ((boolean) processingReturns.get("gotError")) {
            ServerService.deleteTempIcsFiles();
            return processingReturns.get("error");
        }

        else {
            ServerService.deleteTempIcsFiles();
            ServerService.loadCalendars();
            res.redirect("/");
        }

        return "what happened?";
    }

    @Override
    public Map<String, Object> process(Map<String, String> params) {
        String calendarUrl = params.get("calendarLink").trim();

        // Checks if the link protocol is webcal and then it changes it to https for download
        if (!(calendarUrl.startsWith("webcal"))) {
            return buildResponseMap(null, "Please make sure the url is webcal://", false, true);
        }

        calendarUrl = calendarUrl.replace("webcal://", "https://");

        String tempFileName = ServerUtil.generateRandomTempName() + "_temp.ics";
        String tempFilePath = System.getProperty("user.dir") + "/calendars/icsFiles/" + tempFileName;

        // Reading calendar data into temp file
        BufferedInputStream bis = null;
        try {
            bis = new BufferedInputStream(new URL(calendarUrl).openStream());
        } catch (IOException e) {
            return buildResponseMap(null, "Couldn't get calendar data", false, true);
        }

        FileOutputStream fis = null;
        try {
            fis = new FileOutputStream(tempFilePath);
            byte[] buffer = new byte[1024];
            int count = 0;

            while ((count = bis.read(buffer, 0, 1024)) != -1)
                fis.write(buffer, 0, count);

            fis.close();
            bis.close();
        } catch (IOException e) {
            System.err.println("File not found!");
        }

        // Parse the .ics calendar
        try {
            new Parser().initiateCalendar(tempFileName);
        } catch (IOException e) {
            return buildResponseMap(null, "Parser got an error", false, true);
        }

        return buildResponseMap(null, "", false, false);
    }
}
