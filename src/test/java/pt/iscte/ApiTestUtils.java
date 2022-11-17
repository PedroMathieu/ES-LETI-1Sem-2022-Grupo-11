package pt.iscte;

import spark.utils.IOUtils;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiTestUtils {
    public static TestResponse request(String method, String path) throws Exception {
        URL url = new URL("http://localhost:4444" + path);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(method);
        connection.setDoOutput(true);
        connection.connect();
        String body = IOUtils.toString(connection.getInputStream());
        return new TestResponse(connection.getResponseCode(), body);
    }

    public static TestResponse requestPostCalendarInuput(String path, String data) throws Exception {
        //TODO
        return null;
    }

    public static class TestResponse {

        public final String body;
        public final int status;

        public TestResponse(int status, String body) {
            this.status = status;
            this.body = body;
        }
    }
}
