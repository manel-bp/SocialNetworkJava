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
    private SnService service;

    public HttpHandler(SnService service){
        this.service = service;
        try {
            runHttpServer();
            System.out.println("Server is up and running");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void runHttpServer() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(Constants.PORT), 0);

        HttpContext context_signup = server.createContext(Constants.ENDPOINT_SIGNUP);
        context_signup.setHandler(this::handleRequestSignup);

        HttpContext context_login = server.createContext(Constants.ENDPOINT_LOGIN);
        context_login.setHandler(this::handleRequestLogin);

        HttpContext context_reqFriendship = server.createContext(Constants.ENDPOINT_REQUEST_FRIENDSHIP);
        context_reqFriendship.setHandler(this::handleRequestReqFriendship);

        HttpContext context_accFriendship = server.createContext(Constants.ENDPOINT_ACCEPT_FRIENDSHIP);
        context_accFriendship.setHandler(this::handleRequestAccFriendship);

        HttpContext context_decFriendship = server.createContext(Constants.ENDPOINT_DECLINE_FRIENDSHIP);
        context_decFriendship.setHandler(this::handleRequestDecFriendship);

        HttpContext context_listFriends = server.createContext(Constants.ENDPOINT_LIST_FRIENDS);
        context_listFriends.setHandler(this::handleRequestListFriends);
        server.start();
    }

    /**
     * This function will receive a username and password, and will return a message containing
     * if the registration process was done successfully or if there was an error.
     */
    private void handleRequestSignup(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        JsonObject inObj = new JsonParser().parse(body).getAsJsonObject();
        User user = new User(
                getParameter(inObj, Constants.PROPERTY_USERNAME_NAME),
                getParameter(inObj, Constants.PROPERTY_PASSWORD_NAME));

        // And pass it to the service class
        String response = service.signup(user);

        sendResponse(exchange, response);
    }

    /**
     * This function will receive a username and password, and will return a message containing
     * if the login process was done successfully or if there was an error. A Token will be returned
     * for the user to identify himself/herself
     */
    private void handleRequestLogin(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        JsonObject inObj = new JsonParser().parse(body).getAsJsonObject();
        User user = new User(
                getParameter(inObj, Constants.PROPERTY_USERNAME_NAME),
                getParameter(inObj, Constants.PROPERTY_PASSWORD_NAME)
        );

        // And pass it to the service class
        String response = service.login(user);

        sendResponse(exchange, response);
    }

    /**
     * This function will receive a login token identifier and a target user to
     * request friendship. It will return a message containing
     * if the frienship request process was done successfully or if there was an error.
     */
    private void handleRequestReqFriendship(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        JsonObject inObj = new JsonParser().parse(body).getAsJsonObject();
        String token = getParameter(inObj, Constants.PROPERTY_TOKEN_NAME);
        String targetUser = getParameter(inObj, Constants.PROPERTY_TARGET_USER_NAME);

        // And pass it to the service class
        String response = service.requestFriendship(token, targetUser);

        sendResponse(exchange, response);
    }

    /**
     * This function will receive a login token identifier and a target user to
     * request friendship. It will return a message containing
     * if the friendship accepting process was done successfully or if there was an error.
     */
    private void handleRequestAccFriendship(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        JsonObject inObj = new JsonParser().parse(body).getAsJsonObject();
        String token = getParameter(inObj, Constants.PROPERTY_TOKEN_NAME);
        String acceptedUser = getParameter(inObj, Constants.PROPERTY_ACCEPTED_USER_NAME);

        // And pass it to the service class
        String response = service.acceptFriendship(token, acceptedUser);

        sendResponse(exchange, response);
    }

    /**
     * This function will receive a login token identifier and a target user to
     * request friendship. It will return a message containing
     * if the friendship declining process was done successfully or if there was an error.
     */
    private void handleRequestDecFriendship(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        JsonObject inObj = new JsonParser().parse(body).getAsJsonObject();
        String token = getParameter(inObj, Constants.PROPERTY_TOKEN_NAME);
        String declinedUser = getParameter(inObj, Constants.PROPERTY_DECLINED_USER_NAME);

        // And pass it to the service class
        String response = service.declineFriendship(token, declinedUser);

        sendResponse(exchange, response);
    }

    /**
     * This function will receive a login token identifier. It will return a message
     * containing if the friendship declining process was done successfully or if
     * there was an error. A list of friends will be added in the response
     */
    private void handleRequestListFriends(HttpExchange exchange) throws IOException {
        String body = getRequestBody(exchange);

        // Parse information
        JsonObject inObj = new JsonParser().parse(body).getAsJsonObject();
        String token = getParameter(inObj, Constants.PROPERTY_TOKEN_NAME);

        // And pass it to the service class
        String response = service.listFriends(token);

        sendResponse(exchange, response);
    }

    private String getRequestBody(HttpExchange exchange) throws IOException {
        // Get the body of the request, where the parameters are in format json
        StringBuilder sb = new StringBuilder();
        InputStream ios = exchange.getRequestBody();
        int i;
        while ((i = ios.read()) != -1) {
            sb.append((char) i);
        }

        return sb.toString();
    }

    private void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private String getParameter(JsonObject obj, String parameter){
        String value = obj.get(parameter).toString();
        return value.substring(1, value.length() - 1);
    }


}
