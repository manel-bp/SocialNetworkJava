package delivery;

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
        int resp;

        // Try sending a bad username (short)
        params = "{\"username\": \"man\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        Assert.assertEquals(Constants.ERROR_USERNAME_TOO_SHORT, resp);

        // Try sending a bad username (long)
        params = "{\"username\": \"manelbenavides\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        Assert.assertEquals(Constants.ERROR_USERNAME_TOO_LONG, resp);

        // Try sending a bad password (short)
        params = "{\"username\": \"manelbp\", \"password\": \"My\"}";
        resp = executePost("http://localhost:8050/signup", params);
        Assert.assertEquals(Constants.ERROR_PASSWORD_TOO_SHORT, resp);

        // Try sending a bad password (long)
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPassword\"}";
        resp = executePost("http://localhost:8050/signup", params);
        Assert.assertEquals(Constants.ERROR_PASSWORD_TOO_LONG, resp);

        // Try sending a correct signup
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        Assert.assertEquals(Constants.ERROR_SUCCESSFUL, resp);

        // Try sending a bad username (repeated)
        params = "{\"username\": \"manelbp\", \"password\": \"MySecretPass\"}";
        resp = executePost("http://localhost:8050/signup", params);
        Assert.assertEquals(Constants.ERROR_USERNAME_REPEATED, resp);

    }

    public static int executePost(String targetURL, String urlParameters) {
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
            int code = connection.getResponseCode();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if Java version 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            //return response.toString();
            return code;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
