package delivery;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import model.Constants;
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
        HttpServer server = HttpServer.create(new InetSocketAddress(Constants.PORT), 0);

        HttpContext context_signup = server.createContext(Constants.ENDPOINT_SIGNUP);
        context_signup.setHandler(HttpHandler::handleRequestSignup);

        HttpContext context_login = server.createContext(Constants.ENDPOINT_LOGIN);
        context_login.setHandler(HttpHandler::handleRequestLogin);

        HttpContext context_reqFriendship = server.createContext(Constants.ENDPOINT_REQUEST_FRIENDSHIP);
        context_reqFriendship.setHandler(HttpHandler::handleRequestReqFriendship);

        HttpContext context_accFriendship = server.createContext(Constants.ENDPOINT_ACCEPT_FRIENDSHIP);
        context_accFriendship.setHandler(HttpHandler::handleRequestAccFriendship);

        HttpContext context_decFriendship = server.createContext(Constants.ENDPOINT_DECLINE_FRIENDSHIP);
        context_decFriendship.setHandler(HttpHandler::handleRequestDecFriendship);

        HttpContext context_listFriends = server.createContext(Constants.ENDPOINT_LIST_FRIENDS);
        context_listFriends.setHandler(HttpHandler::handleRequestListFriends);
        server.start();
    }

    /**
     * This function will receive a username and password, and will return a message containing
     * if the registration process was done successfully or if there was an error.
     */
    private static void handleRequestSignup(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        Gson g = new Gson();
        User aux = g.fromJson(body, User.class);
        User user = new User(aux.getUsername(), aux.getPassword());

        // And pass it to the service class
        String response = service.signup(user);

        sendResponse(exchange, response);
    }

    /**
     * This function will receive a username and password, and will return a message containing
     * if the login process was done successfully or if there was an error. A Token will be returned
     * for the user to identify himself/herself
     */
    private static void handleRequestLogin(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        Gson g = new Gson();
        User aux = g.fromJson(body, User.class);
        User user = new User(aux.getUsername(), aux.getPassword());

        // And pass it to the service class
        String response = service.login(user);

        sendResponse(exchange, response);
    }

    /**
     * This function will receive a login token identifier and a target user to
     * request friendship. It will return a message containing
     * if the frienship request process was done successfully or if there was an error.
     */
    private static void handleRequestReqFriendship(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        JsonObject inObj = new JsonParser().parse(body).getAsJsonObject();
        String token = getParameter(inObj, "token");
        String targetUser = getParameter(inObj, "targetUser");

        // And pass it to the service class
        String response = service.requestFriendship(token, targetUser);

        sendResponse(exchange, response);
    }

    /**
     * This function will receive a login token identifier and a target user to
     * request friendship. It will return a message containing
     * if the friendship accepting process was done successfully or if there was an error.
     */
    private static void handleRequestAccFriendship(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        JsonObject inObj = new JsonParser().parse(body).getAsJsonObject();
        String token = getParameter(inObj, "token");
        String acceptedUser = getParameter(inObj, "acceptedUser");

        // And pass it to the service class
        String response = service.acceptFriendship(token, acceptedUser);

        sendResponse(exchange, response);
    }

    /**
     * This function will receive a login token identifier and a target user to
     * request friendship. It will return a message containing
     * if the friendship declining process was done successfully or if there was an error.
     */
    private static void handleRequestDecFriendship(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        JsonObject inObj = new JsonParser().parse(body).getAsJsonObject();
        String token = getParameter(inObj, "token");
        String declinedUser = getParameter(inObj, "declinedUser");

        // And pass it to the service class
        String response = service.declineFriendship(token, declinedUser);

        sendResponse(exchange, response);
    }

    /**
     * This function will receive a login token identifier. It will return a message
     * containing if the friendship declining process was done successfully or if
     * there was an error. A list of friends will be added in the response
     */
    private static void handleRequestListFriends(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        JsonObject inObj = new JsonParser().parse(body).getAsJsonObject();
        String token = getParameter(inObj, "token");

        // And pass it to the service class
        String response = service.listFriends(token);

        sendResponse(exchange, response);
    }

    private static String getRequestBody(HttpExchange exchange) throws IOException {
        // Get the body of the request, where the parameters are in format json
        StringBuilder sb = new StringBuilder();
        InputStream ios = exchange.getRequestBody();
        int i;
        while ((i = ios.read()) != -1) {
            sb.append((char) i);
        }

        return sb.toString();
    }

    private static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private static String getParameter(JsonObject obj, String parameter){
        String value = obj.get(parameter).toString();
        return value.substring(1, value.length() - 1);
    }


}
