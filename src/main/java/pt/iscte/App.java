package pt.iscte;

import iscte.server.Server;

import java.io.IOException;

public class App  {
    // List of calendar objects
    private static final Server server = new Server();

    public static void main(String[] args) throws IOException {
        server.init();
        Parser p = new Parser();
        p.readFiles();
    }

}
