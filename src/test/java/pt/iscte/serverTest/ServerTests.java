package pt.iscte.serverTest;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.*;
import pt.iscte.server.Server;
import spark.Spark;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ServerTests {
    private static Server s;

    @Before
    public void setUp() throws Exception {
        s = new Server();
        Spark.awaitInitialization();

    }
    @After
    public void tearDown() throws Exception {
        Spark.stop();
        Spark.awaitStop();
    }

    @Test
    @DisplayName("Calendars uploaded are at least 1")
    public void testCalendarMinimum() {
        assertTrue(s.getPersonalCalendarObjects().size() >= 1);
    }

    @Test
    @DisplayName("Test if server is alive")
    public void isAlive() throws Exception {
        ApiTestUtils.TestResponse res = ApiTestUtils.request("GET", "/");
        assertEquals(200, res.status);
    }
}
