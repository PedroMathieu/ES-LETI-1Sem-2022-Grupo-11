package pt.iscte.server.controllers;

import org.eclipse.jetty.util.ajax.JSON;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.iscte.server.Server;
import pt.iscte.server.ServerTestHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetEventsControllerTest {
    Controller ge = new GetEventsController(ServerTestHelper.buildTestCalendars());

    @Test
    public void givenDatesThatAreNaN_whenGettingEvents_returnsError() {
        Map<String, String> params0 = ServerTestHelper.buildGetEventsParams("test0-test1", "e", "a", "12", "1");
        Map<String, String> params1 = ServerTestHelper.buildGetEventsParams("test0-test1", "e", "2022", "3.12", "1");
        List<Map<String, Object>> responses = new ArrayList<>();
        responses.add(ge.process(params0));
        responses.add(ge.process(params1));

        for (Map<String, Object> r : responses) {
            String errorText = (String) r.get("error");
            Assertions.assertEquals(true, (boolean) r.get("gotError"));
            Assertions.assertTrue(errorText.contains("not a number"));
        }
    }

    @Test
    public void givenWrongOwners_whenGettingEvents_returnsError() {
        Map<String, String> params0 = ServerTestHelper.buildGetEventsParams("foo-bar", "e", "2022", "11", "28");
        Map<String, String> params1 = ServerTestHelper.buildGetEventsParams("foo", "e", "2022", "11", "28");
        List<Map<String, Object>> responses = new ArrayList<>();
        responses.add(ge.process(params0));
        responses.add(ge.process(params1));

        for (Map<String, Object> r : responses) {
            String errorText = (String) r.get("error");
            Assertions.assertEquals(true, (boolean) r.get("gotError"));
            Assertions.assertTrue(errorText.contains("Parameters contain problems"));
        }
    }

    @Test
    public void givenNonExistentOperation_whenGettingEvents_returnsError() {
        Map<String, String> params0 = ServerTestHelper.buildGetEventsParams("test0-test1", "x", "2022", "11", "28");
        Map<String, Object> response = ge.process(params0);

        String errorText = (String) response.get("error");
        Assertions.assertTrue(errorText.contains("Selected operation does not exist"));
        Assertions.assertEquals(true, (boolean) response.get("gotError"));
    }

    @Test
    public void givenRequestNumberOfEvents_whenGettingEvents_returnsContextJson() {
        Map<String, String> params0 = ServerTestHelper.buildGetEventsParams("test0-test1", "n", "2022", "11", "28");
        Map<String, Object> response = ge.process(params0);

        Assertions.assertEquals(true, (boolean) response.get("contextJson"));
    }

    @Test
    public void givenCorrectParamsToGetEvents_whenGettingEvents_returnsCorrectData() {
        Map<String, String> params0 = ServerTestHelper.buildGetEventsParams("test0-test1", "e", "2022", "10", "27");
        Map<String, Object> response = ge.process(params0);

        // These 2 lines are very weird, but they work to make sure everything is working
        JSONObject dataToSend = (JSONObject) response.get("dataToSend");
        int actualNumberOfEvents = ((JSONArray)((JSONObject) dataToSend.get("test0")).get("events")).size();

        Assertions.assertEquals(2, actualNumberOfEvents);
        Assertions.assertEquals(false, (boolean) response.get("gotError"));
        Assertions.assertEquals(false, (boolean) response.get("contextJson"));
        Assertions.assertEquals("", (String) response.get("error"));
    }

    @Test
    public void givenCorrectParamsToGetNOfEvents_whenGettingEvents_returnsCorrectData() {
        Map<String, String> params0 = ServerTestHelper.buildGetEventsParams("test0-test1", "n", "2022", "10", "27");
        Map<String, Object> response = ge.process(params0);

        JSONObject dataToSend = (JSONObject) response.get("dataToSend");

        Assertions.assertEquals(2, (Integer) dataToSend.get("test0"));
        Assertions.assertEquals(2, (Integer) dataToSend.get("test1"));
        Assertions.assertEquals(false, (boolean) response.get("gotError"));
        Assertions.assertEquals(true, (boolean) response.get("contextJson"));
        Assertions.assertEquals("", (String) response.get("error"));
    }
}
