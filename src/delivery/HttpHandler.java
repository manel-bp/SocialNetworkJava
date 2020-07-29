package delivery;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.User;
import service.SnService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 * The HTTP Handler is the class that contains the HTTP Server and its endpoints.
 * Its function is just receive a request and tell to the Service (the brain) to do whatever it needs to.
 */
public class HttpHandler {
    private static SnService service;

    public HttpHandler(){
        service = new SnService();
        try {
            runHttpServer();
            System.out.println("Up and running");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void runHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8050), 0);

        HttpContext context_signup = server.createContext("/signup");
        context_signup.setHandler(HttpHandler::handleRequestSignup);

        HttpContext context_reqFriendship = server.createContext("/request-friendship");
        context_reqFriendship.setHandler(HttpHandler::handleRequestReqFriendship);

        HttpContext context_accFriendship = server.createContext("/accept-friendship");
        context_accFriendship.setHandler(HttpHandler::handleRequestAccFriendship);

        HttpContext context_decFriendship = server.createContext("/decline-friendship");
        context_decFriendship.setHandler(HttpHandler::handleRequestDecFriendship);

        HttpContext context_listFriends = server.createContext("/list-friends");
        context_listFriends.setHandler(HttpHandler::handleRequestListFriends);
        server.start();
    }

    private static void handleRequestSignup(HttpExchange exchange) throws IOException {
        /**
         * This function will receive a username and password, and will return a message containing
         * if the process was done successfully or if there was an error.
         */
        // Get the body of the request, where the parameters are in format json
        StringBuilder sb = new StringBuilder();
        InputStream ios = exchange.getRequestBody();
        int i;
        while ((i = ios.read()) != -1) {
            sb.append((char) i);
        }

        // And pass it to the service class
        int error = service.signup(sb.toString());
        JsonObject obj = new JsonObject();
        obj.addProperty("code", error);
        obj.addProperty("message", service.getErrorName(error));
        String response = obj.toString();

        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestReqFriendship(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getHttpContext().getPath());
        String response = "Hi there req!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestAccFriendship(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getHttpContext().getPath());
        String response = "Hi there acc!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestDecFriendship(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getHttpContext().getPath());
        String response = "Hi there dec!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static void handleRequestListFriends(HttpExchange exchange) throws IOException {
        System.out.println(exchange.getHttpContext().getPath());
        String response = "Hi there list!";
        service.addUser(new User("hola", "adeu"));
        service.listUsers();
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

}
