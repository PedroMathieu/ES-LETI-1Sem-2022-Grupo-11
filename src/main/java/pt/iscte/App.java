package pt.iscte;

import java.io.IOException;

public class App  {
    // List of calendar objects
    private static Server server = new Server();

    public static void main(String[] args) throws IOException {
        server.init();
        Parser p = new Parser();
        p.readFiles();
    }
}
