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

    }

    @Test
    public void test_Login(){
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
