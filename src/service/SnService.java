package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.Constants;
import model.User;
import repository.Singleton;

import java.util.ArrayList;
import java.util.Random;

/**
 * The Service class will parse the information, check for possible errors and implement
 * the solution for each of the functionalities
 */
public class SnService {

    // Constructor
    public SnService(){}

    public String signup(String body){
        // Parse information
        Gson g = new Gson();
        User user = g.fromJson(body, User.class);

        // Check possible errors
        // Length of username
        if (user.getUsername().length() > Constants.MAX_USERNAME_LENGTH){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_USERNAME_TOO_LONG);
            obj.addProperty("message", Constants.ERROR_USERNAME_TOO_LONG_TEXT);
            String response = obj.toString();
            return response;

        }else if (user.getUsername().length() < Constants.MIN_USERNAME_LENGTH){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_USERNAME_TOO_SHORT);
            obj.addProperty("message", Constants.ERROR_USERNAME_TOO_SHORT_TEXT);
            String response = obj.toString();
            return response;
        }

        // Length of password
        if (user.getPassword().length() > Constants.MAX_PASSWORD_LENGTH){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_PASSWORD_TOO_LONG);
            obj.addProperty("message", Constants.ERROR_PASSWORD_TOO_LONG_TEXT);
            String response = obj.toString();
            return response;

        }else if (user.getPassword().length() < Constants.MIN_PASSWORD_LENGTH){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_PASSWORD_TOO_SHORT);
            obj.addProperty("message", Constants.ERROR_PASSWORD_TOO_SHORT_TEXT);
            String response = obj.toString();
            return response;
        }

        // User already exists
        if (Singleton.getInstance().exists(user.getUsername())){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_USERNAME_REPEATED);
            obj.addProperty("message", Constants.ERROR_USERNAME_REPEATED_TEXT);
            String response = obj.toString();
            return response;
        }

        // If none of the errors was found, sign up the person
        Singleton.getInstance().addUser(user);
        JsonObject obj = new JsonObject();
        obj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        obj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);
        String response = obj.toString();
        return response;
    }

    public String login(String body){
        // Parse information
        Gson g = new Gson();
        User user = g.fromJson(body, User.class);

        // Check possible errors
        // The user exists with the same username and password
        if (Singleton.getInstance().checkLogin(user.getUsername(), user.getPassword())){
            // Generate Token
            int leftLimit = 97; // letter 'a'
            int rightLimit = 122; // letter 'z'
            int targetStringLength = 10;
            Random random = new Random();
            StringBuilder buffer = new StringBuilder(targetStringLength);
            for (int i = 0; i < targetStringLength; i++) {
                int randomLimitedInt = leftLimit + (int)
                        (random.nextFloat() * (rightLimit - leftLimit + 1));
                buffer.append((char) randomLimitedInt);
            }
            String token = buffer.toString();

            // Generate response
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_SUCCESSFUL);
            obj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);
            obj.addProperty("token", token);
            String response = obj.toString();
            return response;
        }else{
            // Generate response
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_LOGIN);
            obj.addProperty("message", Constants.ERROR_LOGIN_TEXT);
            String response = obj.toString();
            return response;
        }
    }

    // Public methods
    public void addUser(User user){
        Singleton.getInstance().addUser(user);
    }

    public void listUsers(){
        ArrayList<User> allUsers = Singleton.getInstance().listUsers();
        for (int i = 0; i < allUsers.size(); i++){
            System.out.println(allUsers.get(i).getUsername());
        }
    }

}
