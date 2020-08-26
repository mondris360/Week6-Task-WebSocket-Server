import Threads.Server;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            // start the server
            Server server = new Server(3000);
            server.start();
        } catch(IOException e){
            e.getMessage();
        }
    }
}
