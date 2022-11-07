package pt.iscte;

import java.io.IOException;

public class App  {
    // List of calendar objects
    private static Server server;

    public static void main(String[] args) throws IOException {
        server = new Server();
        Parser p = new Parser();
        p.readFiles();
    }
}
