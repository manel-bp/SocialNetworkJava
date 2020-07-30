package delivery;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * This class will contain the test of the different modules
 */
public class HttpHandler_Test {

    /**
     * Testing the signup consists on a stack of tests:
     */
    @Test
    public void test_Signup(){
        System.out.println("Testing signup...");
        // Create an instance of the HttpServer
        new HttpHandler();
        String params;

        // Try sending a bad username (short)
        params = "{\"username\": \"man\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_USERNAME_TOO_SHORT, Constants.ERROR_USERNAME_TOO_SHORT_TEXT);

        // Try sending a bad username (long)
        params = "{\"username\": \"manelbenavides\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_USERNAME_TOO_LONG, Constants.ERROR_USERNAME_TOO_LONG_TEXT);


        // Try sending a bad password (short)
        params = "{\"username\": \"manelbp\", \"password\": \"My\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_PASSWORD_TOO_SHORT, Constants.ERROR_PASSWORD_TOO_SHORT_TEXT);

        // Try sending a bad password (long)
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPassword\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_PASSWORD_TOO_LONG, Constants.ERROR_PASSWORD_TOO_LONG_TEXT);

        // Try sending a correct signup
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Try sending a bad username (repeated)
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_USERNAME_REPEATED, Constants.ERROR_USERNAME_REPEATED_TEXT);

        System.out.println("Signup tested SUCCESSFULLY");
    }

    @Test
    public void test_Login(){
        System.out.println("Testing login...");

        // Create an instance of the HttpServer
        new HttpHandler();
        String params;

        // Try sending a username that does not exist yet
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_LOGIN, params,
                Constants.ERROR_LOGIN, Constants.ERROR_LOGIN_TEXT);

        // Now Sign up and login correctly
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        checkRequestData(Constants.LONG_ENDPOINT_LOGIN, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        System.out.println("Login tested SUCCESSFULLY");
    }

    @Test
    public void test_RequestFriendship(){
        System.out.println("Testing friendship request...");

        // Create an instance of the HttpServer
        new HttpHandler();
        String params;

        // Sign up two users
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Login to one of the accounts
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        String userToken = checkRequestData(Constants.LONG_ENDPOINT_LOGIN, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Try with invalid token
        String fakeToken = "holaquetal";
        params = "{\"token\": \"" + fakeToken + "\", \"targetUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_INCORRECT_TOKEN, Constants.ERROR_INCORRECT_TOKEN_TEXT);

        // Try with invalid target user
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"marcvp\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_INCORRECT_USERNAME, Constants.ERROR_INCORRECT_USERNAME_TEXT);

        // Try a friendship request to himself/herself
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"manelbp\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_INCORRECT_FRIENDSHIP_REQUEST, Constants.ERROR_INCORRECT_FRIENDSHIP_REQUEST_TEXT);

        // A correct friendship request
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Try to repeat a friendship request that already exists
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT, Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT_TEXT);

        System.out.println("Friendship request tested SUCCESSFULLY");
    }

    @Test
    public void test_AcceptFriendshipRequest(){
        System.out.println("Testing friendship accept...");

        // Create an instance of the HttpServer
        new HttpHandler();
        String params;

        // Sign up two users
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Login to both the accounts
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        String userToken = checkRequestData(Constants.LONG_ENDPOINT_LOGIN, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        String userToken2 = checkRequestData(Constants.LONG_ENDPOINT_LOGIN, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Try with invalid token
        String fakeToken = "holaquetal";
        params = "{\"token\": \"" + fakeToken + "\", \"acceptedUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_ACCEPT_FRIENDSHIP, params,
                Constants.ERROR_INCORRECT_TOKEN, Constants.ERROR_INCORRECT_TOKEN_TEXT);

        // Try with invalid accepted user
        params = "{\"token\": \"" + userToken + "\", \"acceptedUser\": \"marcvp\"}";
        checkRequest(Constants.LONG_ENDPOINT_ACCEPT_FRIENDSHIP, params,
                Constants.ERROR_INCORRECT_USERNAME, Constants.ERROR_INCORRECT_USERNAME_TEXT);

        // Accept a friendship request that does not exist
        params = "{\"token\": \"" + userToken + "\", \"acceptedUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_ACCEPT_FRIENDSHIP, params,
                Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST, Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST_TEXT);

        // Friendship is requested
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Accept a friendship request correctly
        params = "{\"token\": \"" + userToken2 + "\", \"acceptedUser\": \"manelbp\"}";
        checkRequest(Constants.LONG_ENDPOINT_ACCEPT_FRIENDSHIP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Friendship is requested to a friend
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_FRIENDSHIP_REQUEST_TO_A_FRIEND, Constants.ERROR_FRIENDSHIP_REQUEST_TO_A_FRIEND_TEXT);

        System.out.println("Accept friendship tested SUCCESSFULLY");
    }

    @Test
    public void test_DeclineFriendshipRequest() {
        System.out.println("Testing friendship decline...");

        // Create an instance of the HttpServer
        new HttpHandler();
        String params;

        // Sign up two users
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Login to both the accounts
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        String userToken = checkRequestData(Constants.LONG_ENDPOINT_LOGIN, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        String userToken2 = checkRequestData(Constants.LONG_ENDPOINT_LOGIN, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Try with invalid token
        String fakeToken = "holaquetal";
        params = "{\"token\": \"" + fakeToken + "\", \"declinedUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_DECLINE_FRIENDSHIP, params,
                Constants.ERROR_INCORRECT_TOKEN, Constants.ERROR_INCORRECT_TOKEN_TEXT);

        // Try with invalid declined user
        params = "{\"token\": \"" + userToken2 + "\", \"declinedUser\": \"marcvp\"}";
        checkRequest(Constants.LONG_ENDPOINT_DECLINE_FRIENDSHIP, params,
                Constants.ERROR_INCORRECT_USERNAME, Constants.ERROR_INCORRECT_USERNAME_TEXT);

        // Friendship request does not exist yet
        params = "{\"token\": \"" + userToken2 + "\", \"declinedUser\": \"manelbp\"}";
        checkRequest(Constants.LONG_ENDPOINT_DECLINE_FRIENDSHIP, params,
                Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST, Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST_TEXT);

        // Friendship is requested
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Friendship is requested again
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT, Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT_TEXT);

        // Friendship is declined
        params = "{\"token\": \"" + userToken2 + "\", \"declinedUser\": \"manelbp\"}";
        checkRequest(Constants.LONG_ENDPOINT_DECLINE_FRIENDSHIP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Friendship is requested again
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        System.out.println("Decline friendship was tested SUCCESSFULLY");
    }

    @Test
    public void test_ListFriends(){
        System.out.println("Testing list friends...");

        // Create an instance of the HttpServer
        new HttpHandler();
        String params;
        String listOfFriends;

        // Sign up three users
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"username\": \"marcvp\", \"password\": \"MySecretPass\"}";
        checkRequest(Constants.LONG_ENDPOINT_SIGNUP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Login to all three accounts
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        String userToken = checkRequestData(Constants.LONG_ENDPOINT_LOGIN, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        String userToken2 = checkRequestData(Constants.LONG_ENDPOINT_LOGIN, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"username\": \"marcvp\", \"password\": \"MySecretPass\"}";
        String userToken3 = checkRequestData(Constants.LONG_ENDPOINT_LOGIN, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Try with invalid token
        String fakeToken = "holaquetal";
        params = "{\"token\": \"" + fakeToken + "\"}";
        checkRequest(Constants.LONG_ENDPOINT_LIST_FRIENDS, params,
                Constants.ERROR_INCORRECT_TOKEN, Constants.ERROR_INCORRECT_TOKEN_TEXT);

        // List friends (zero friends)
        params = "{\"token\": \"" + userToken + "\"}";
        listOfFriends = "[]";
        checkRequestParam(Constants.LONG_ENDPOINT_LIST_FRIENDS, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT, listOfFriends);

        // Request friendships
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"marcvp\"}";
        checkRequest(Constants.LONG_ENDPOINT_REQUEST_FRIENDSHIP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // Accept friendships
        params = "{\"token\": \"" + userToken2 + "\", \"acceptedUser\": \"manelbp\"}";
        checkRequest(Constants.LONG_ENDPOINT_ACCEPT_FRIENDSHIP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        params = "{\"token\": \"" + userToken3 + "\", \"acceptedUser\": \"manelbp\"}";
        checkRequest(Constants.LONG_ENDPOINT_ACCEPT_FRIENDSHIP, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);

        // List friends (all friends)
        params = "{\"token\": \"" + userToken + "\"}";
        listOfFriends = "[carlosrc, marcvp]";
        checkRequestParam(Constants.LONG_ENDPOINT_LIST_FRIENDS, params,
                Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT, listOfFriends);

        System.out.println("List friends tested SUCCESSFULLY");
    }

    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.close();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            return response.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void checkRequest(String endpoint, String params, int code, String message) {
        String resp = executePost(endpoint, params);

        JsonObject recvObj;
        JsonObject targetObj;

        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", code);
        targetObj.addProperty("message", message);

        Assert.assertEquals(targetObj, recvObj);
    }

    private String checkRequestData(String endpoint, String params, int code, String message) {
        String resp = executePost(endpoint, params);

        JsonObject recvObj;
        JsonObject targetObj;

        recvObj = new JsonParser().parse(resp).getAsJsonObject();
        String data = getParameter(recvObj, "data");
        recvObj.remove("data");

        targetObj = new JsonObject();
        targetObj.addProperty("code", code);
        targetObj.addProperty("message", message);

        Assert.assertEquals(targetObj, recvObj);

        return data;
    }

    private void checkRequestParam(String endpoint, String params, int code, String message, String param) {
        String resp = executePost(endpoint, params);

        JsonObject recvObj;
        JsonObject targetObj;

        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", code);
        targetObj.addProperty("message", message);
        targetObj.addProperty("data", param);

        Assert.assertEquals(targetObj, recvObj);
    }

    private String getParameter(JsonObject obj, String parameter){
        String value = obj.get(parameter).toString();
        return value.substring(1, value.length() - 1);
    }
}
