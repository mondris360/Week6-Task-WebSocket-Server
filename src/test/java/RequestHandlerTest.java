import Threads.Server;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestHandlerTest {
    Server serve;
 @BeforeEach
    void setUp(){
     try {
          serve  = new Server(3000);
          serve.start();
     } catch (IOException e) {
     }
 }

 @Test
 // server should be up and running
 void serverShouldBeLive(){
     HttpRequest request = HttpRequest.newBuilder()
             .uri(URI.create("http://localhost:3000/json")).build();
     Assertions.assertTrue(serve.isAlive());
 }

 @Test
 // server should send a valid response headers
 void shouldSendValidHeaders() {
     HttpRequest request =  HttpRequest.newBuilder()
             .uri(URI.create("http://localhost:3000/json")).build();
     HttpClient client = HttpClient.newHttpClient();
     HttpResponse<String> response;
     try{
         response = client.send(request, HttpResponse.BodyHandlers.ofString());
         int statusCode = response.statusCode();
         String contentType = response.headers().allValues("Content-type").get(0);

         Assertions.assertAll(
                 ()-> assertEquals(200, statusCode),
                 () -> assertEquals("application/json", contentType)
         );

     } catch(IOException |InterruptedException e){
         fail("Should have returned value response headers");
         e.printStackTrace();
     }

 }

 @Test
 // client should not be able to connect to the server on an Invalid port number
 void clientShouldNotConnectOnAWrongPort() {
     HttpRequest request =  HttpRequest.newBuilder()
             .uri(URI.create("http://localhost:5000/json")).build();
     HttpClient client = HttpClient.newHttpClient();

        Assertions.assertThrows(java.net.ConnectException.class, ()->{
            client.send(request, HttpResponse.BodyHandlers.ofString());
        });


 }
 @Test
 // should send the right json file content
    void shouldSendCorrectJsonFileContent(){
     HttpRequest request =  HttpRequest.newBuilder()
             .uri(URI.create("http://localhost:3000/json")).build();
     HttpClient client = HttpClient.newHttpClient();
     HttpResponse<String> response;


     try {
         response = client.send(request, HttpResponse.BodyHandlers.ofString());
         Path fileLocation = Paths.get("./src/files/quiz.json");
         List<String> expectedFileContent = Files.readAllLines(fileLocation);
         String expected  =  response.body();
         System.out.println("response " + expected.replaceAll("\\s", ""));
         System.out.println("expectedFileContent " + expectedFileContent);
//         System.out.println(response.body().replaceAll("\\s", ""));
         fail("Will Fix this latter");

     } catch (IOException e) {
         e.printStackTrace();
     } catch (InterruptedException e) {
         e.printStackTrace();
     }
 }


    @Test
        // Should send the correct html content
    void shouldSendCorrectHtmlFileContent(){
        HttpRequest request =  HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/json")).build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response;


        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            Path fileLocation = Paths.get("./src/files/quiz.json");
            List<String> expectedFileContent = Files.readAllLines(fileLocation);
            String expected  =  response.body();
            System.out.println("response " + expected.replaceAll("\\s", ""));
            System.out.println("expectedFileContent " + expectedFileContent);
//         System.out.println(response.body().replaceAll("\\s", ""));
            fail("Will Fix this latter");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}