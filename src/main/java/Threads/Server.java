package Threads;

import Threads.RequestHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;

public class Server extends Thread {
    ServerSocket server;

    public Server(int port) throws IOException {
        this.server =  new ServerSocket(port);
    }

    @Override
    public void run()  {
       try {
           System.out.println("Threads.Server Is Running on Port: " + server.getLocalPort());
           while(true){
               // create a new Thread to handle the new  client request
               Thread requestHandler =  new Thread(new RequestHandler(server.accept()));
               requestHandler.start();
           }
       }catch (IOException e){
           e.getMessage();
       } finally {
           try {
               server.close();
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
    }
}
