package delivery;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.Constants;
import org.junit.Assert;
import org.junit.Test;

import java.awt.desktop.SystemSleepEvent;
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
        String resp;
        JsonObject recvObj;
        JsonObject targetObj;

        // Try sending a bad username (short)
        params = "{\"username\": \"man\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_USERNAME_TOO_SHORT);
        targetObj.addProperty("message", Constants.ERROR_USERNAME_TOO_SHORT_TEXT);

        Assert.assertEquals(targetObj, recvObj);


        // Try sending a bad username (long)
        params = "{\"username\": \"manelbenavides\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_USERNAME_TOO_LONG);
        targetObj.addProperty("message", Constants.ERROR_USERNAME_TOO_LONG_TEXT);

        Assert.assertEquals(targetObj, recvObj);


        // Try sending a bad password (short)
        params = "{\"username\": \"manelbp\", \"password\": \"My\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_PASSWORD_TOO_SHORT);
        targetObj.addProperty("message", Constants.ERROR_PASSWORD_TOO_SHORT_TEXT);

        Assert.assertEquals(targetObj, recvObj);


        // Try sending a bad password (long)
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPassword\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_PASSWORD_TOO_LONG);
        targetObj.addProperty("message", Constants.ERROR_PASSWORD_TOO_LONG_TEXT);

        Assert.assertEquals(targetObj, recvObj);


        // Try sending a correct signup
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);


        // Try sending a bad username (repeated)
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_USERNAME_REPEATED);
        targetObj.addProperty("message", Constants.ERROR_USERNAME_REPEATED_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        System.out.println("Signup tested SUCCESSFULLY");
    }

    @Test
    public void test_Login(){
        System.out.println("Testing login...");

        // Create an instance of the HttpServer
        new HttpHandler();
        String params;
        String resp;
        JsonObject recvObj;
        JsonObject targetObj;

        // Try sending a username that does not exist yet
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/login", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_LOGIN);
        targetObj.addProperty("message", Constants.ERROR_LOGIN_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Now Sign up and login correctly
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/login", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();
        recvObj.remove("token");

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        System.out.println("Login tested SUCCESSFULLY");
    }

    @Test
    public void test_RequestFriendship(){
        System.out.println("Testing friendship request...");

        // Create an instance of the HttpServer
        new HttpHandler();
        String params;
        String resp;
        JsonObject recvObj;
        JsonObject targetObj;

        // Sign up two users
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Login to one of the accounts
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/login", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);
        String userToken = recvObj.get("token").toString();
        userToken = userToken.substring(1, userToken.length() - 1);
        recvObj.remove("token");
        Assert.assertEquals(targetObj, recvObj);

        // Try with invalid token
        String fakeToken = "holaquetal";
        params = "{\"token\": \"" + fakeToken + "\", \"targetUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/request-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_INCORRECT_TOKEN);
        targetObj.addProperty("message", Constants.ERROR_INCORRECT_TOKEN_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Try with invalid target user
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"marcvp\"}";
        resp = executePost("http://localhost:8050/request-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_INCORRECT_USERNAME);
        targetObj.addProperty("message", Constants.ERROR_INCORRECT_USERNAME_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Try a friendship request to yourself
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"manelbp\"}";
        resp = executePost("http://localhost:8050/request-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_INCORRECT_FRIENDSHIP_REQUEST);
        targetObj.addProperty("message", Constants.ERROR_INCORRECT_FRIENDSHIP_REQUEST_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // A correct friendship request
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/request-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Try to repeat a friendship request that already exists
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/request-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT);
        targetObj.addProperty("message", Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        System.out.println("Friendship request tested SUCCESSFULLY");
    }

    @Test
    public void test_AcceptFriendshipRequest(){
        System.out.println("Testing friendship accept...");

        // Create an instance of the HttpServer
        new HttpHandler();
        String params;
        String resp;
        JsonObject recvObj;
        JsonObject targetObj;

        // Sign up two users
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Login to both the accounts
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/login", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);
        String userToken = recvObj.get("token").toString();
        userToken = userToken.substring(1, userToken.length() - 1);
        recvObj.remove("token");
        Assert.assertEquals(targetObj, recvObj);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/login", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);
        String userToken2 = recvObj.get("token").toString();
        userToken2 = userToken2.substring(1, userToken2.length() - 1);
        recvObj.remove("token");
        Assert.assertEquals(targetObj, recvObj);

        // Try with invalid token
        String fakeToken = "holaquetal";
        params = "{\"token\": \"" + fakeToken + "\", \"acceptedUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/accept-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_INCORRECT_TOKEN);
        targetObj.addProperty("message", Constants.ERROR_INCORRECT_TOKEN_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Try with invalid accepted user
        params = "{\"token\": \"" + userToken + "\", \"acceptedUser\": \"marcvp\"}";
        resp = executePost("http://localhost:8050/accept-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_INCORRECT_USERNAME);
        targetObj.addProperty("message", Constants.ERROR_INCORRECT_USERNAME_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Accept a friendship request that does not exist
        params = "{\"token\": \"" + userToken + "\", \"acceptedUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/accept-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST);
        targetObj.addProperty("message", Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Friendship is requested
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/request-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Accept a friendship request correctly
        params = "{\"token\": \"" + userToken2 + "\", \"acceptedUser\": \"manelbp\"}";
        resp = executePost("http://localhost:8050/accept-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Friendship is requested to a friend
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/request-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_FRIENDSHIP_REQUEST_TO_A_FRIEND);
        targetObj.addProperty("message", Constants.ERROR_FRIENDSHIP_REQUEST_TO_A_FRIEND_TEXT);

        Assert.assertEquals(targetObj, recvObj);
    }

    @Test
    public void test_DeclineFriendshipRequest() {
        System.out.println("Testing friendship decline...");

        // Create an instance of the HttpServer
        new HttpHandler();
        String params;
        String resp;
        JsonObject recvObj;
        JsonObject targetObj;

        // Sign up two users
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Login to both the accounts
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/login", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);
        String userToken = recvObj.get("token").toString();
        userToken = userToken.substring(1, userToken.length() - 1);
        recvObj.remove("token");
        Assert.assertEquals(targetObj, recvObj);

        params = "{\"username\": \"carlosrc\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/login", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);
        String userToken2 = recvObj.get("token").toString();
        userToken2 = userToken2.substring(1, userToken2.length() - 1);
        recvObj.remove("token");
        Assert.assertEquals(targetObj, recvObj);

        // Try with invalid token
        String fakeToken = "holaquetal";
        params = "{\"token\": \"" + fakeToken + "\", \"declinedUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/decline-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_INCORRECT_TOKEN);
        targetObj.addProperty("message", Constants.ERROR_INCORRECT_TOKEN_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Try with invalid declined user
        params = "{\"token\": \"" + userToken2 + "\", \"declinedUser\": \"marcvp\"}";
        resp = executePost("http://localhost:8050/decline-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_INCORRECT_USERNAME);
        targetObj.addProperty("message", Constants.ERROR_INCORRECT_USERNAME_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Friendship request does not exist yet
        params = "{\"token\": \"" + userToken2 + "\", \"declinedUser\": \"manelbp\"}";
        resp = executePost("http://localhost:8050/decline-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST);
        targetObj.addProperty("message", Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Friendship is requested
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/request-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Friendship is requested again
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/request-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT);
        targetObj.addProperty("message", Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Friendship is declined
        params = "{\"token\": \"" + userToken2 + "\", \"declinedUser\": \"manelbp\"}";
        resp = executePost("http://localhost:8050/decline-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);

        // Friendship is requested again
        params = "{\"token\": \"" + userToken + "\", \"targetUser\": \"carlosrc\"}";
        resp = executePost("http://localhost:8050/request-friendship", params);
        recvObj = new JsonParser().parse(resp).getAsJsonObject();

        targetObj = new JsonObject();
        targetObj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        targetObj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);

        Assert.assertEquals(targetObj, recvObj);
    }

    public static String executePost(String targetURL, String urlParameters) {
        HttpURLConnection connection = null;

        try {
            //Create connection
            URL url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length",
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream());
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
}
