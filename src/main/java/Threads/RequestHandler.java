package Threads;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestHandler extends   Thread {
    private Socket socket;
    // constructor
    public RequestHandler(Socket socket) throws IOException {
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            System.out.println("Client Connected");
            BufferedReader requestReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String clientInput = requestReader.readLine();
            System.out.println("Incoming Request: " +  clientInput);
            String[] clientInputToArray = clientInput.split(" ");
            // get the requested route
            String route = clientInputToArray[1];
            // get the location of the file to  render
            String getFileLocation = findRouteResources(route);
            // get the appropriate status code
            String statusCode =  getFileLocation.equals("./src/files/404.html") ? "404 Not Found": "200 OK";
            // send the response to the client
            renderPage (socket, getFileLocation, statusCode);
        } catch(IOException e){
            e.printStackTrace();
        } finally {
            try {
                socket.close();
                System.out.println("Released Client Connection");
            } catch (IOException e) {
                System.out.println("Unable To Release Client Connection");
                e.printStackTrace();
            }
        }
    }

    // method to render the web page
    private  static void renderPage(Socket socket, String filePath,String statusCode)  {
        // get the file path
        Path fileLocation = Paths.get(filePath);
        String CRLF ="\r\n";
        try(OutputStream responseWriter =  socket.getOutputStream()) {
            // get the file content type

            String contentType =  Files.probeContentType(fileLocation);
            // read the entire file content as byte
            byte[] fileContent =  Files.readAllBytes(fileLocation);

            // prepare the http response
            responseWriter.write(("HTTP/1.1 " + statusCode + CRLF).getBytes());
            System.out.println("Status =========" +  statusCode);
            responseWriter.write(("Content-Type: " + contentType + CRLF).getBytes());
            responseWriter.write(( CRLF).getBytes());
//            responseWriter.write(("Content-Length:" + fileContent.length + CRLF + CRLF).getBytes());
            responseWriter.write(fileContent);
            responseWriter.write((CRLF + CRLF).getBytes());
            responseWriter.flush();

        } catch (IOException e){
            System.out.println("Unable to read file content");
            e.getMessage();

        }
    }

   // method to get the location of the requested resources
    private  String findRouteResources( String route){
        String fileLocation = "";
        switch (route){
            case "/":
                fileLocation = "./src/files/home.html";
                break;
            case "/json":
                fileLocation = "./src/files/quiz.json";
                break;

            default:
                fileLocation = "./src/files/404.html";
                break;
        }
        return fileLocation;
    }
}
