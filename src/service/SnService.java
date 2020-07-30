package service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

    public String signup(User user){

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

    public String login(User user){
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
            Singleton.getInstance().setLoginToken(user.getUsername(), token);

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

    public String requestFriendship(String token, String targetUser){

        // Check possible errors
        // The origin user exists (token is correct)
        String originUser = Singleton.getInstance().getUsernameFromToken(token);
        if (originUser == null){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_INCORRECT_TOKEN);
            obj.addProperty("message", Constants.ERROR_INCORRECT_TOKEN_TEXT);
            String response = obj.toString();
            return response;
        }

        // The target user exists
        if (!Singleton.getInstance().exists(targetUser)){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_INCORRECT_USERNAME);
            obj.addProperty("message", Constants.ERROR_INCORRECT_USERNAME_TEXT);
            String response = obj.toString();
            return response;
        }

        // Target user is not the same one as the origin user
        if (originUser.equals(targetUser)){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_INCORRECT_FRIENDSHIP_REQUEST);
            obj.addProperty("message", Constants.ERROR_INCORRECT_FRIENDSHIP_REQUEST_TEXT);
            String response = obj.toString();
            return response;
        }

        // A pending friendship request does not exist yet
        if (Singleton.getInstance().existsFriendshipRequest(originUser, targetUser)){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT);
            obj.addProperty("message", Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT_TEXT);
            String response = obj.toString();
            return response;
        }

        // Both users are not friends yet
        if (Singleton.getInstance().existsFriendship(originUser, targetUser)){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_FRIENDSHIP_REQUEST_TO_A_FRIEND);
            obj.addProperty("message", Constants.ERROR_FRIENDSHIP_REQUEST_TO_A_FRIEND_TEXT);
            String response = obj.toString();
            return response;
        }

        Singleton.getInstance().addFriendshipRequest(originUser, targetUser);
        JsonObject obj = new JsonObject();
        obj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        obj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);
        String response = obj.toString();
        return response;
    }

    public String acceptFriendship(String token, String acceptedUser){
        // Check possible errors
        // The origin user exists (token is correct)
        String originUser = Singleton.getInstance().getUsernameFromToken(token);
        if (originUser == null){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_INCORRECT_TOKEN);
            obj.addProperty("message", Constants.ERROR_INCORRECT_TOKEN_TEXT);
            String response = obj.toString();
            return response;
        }

        // The accepted user exists
        if (!Singleton.getInstance().exists(acceptedUser)){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_INCORRECT_USERNAME);
            obj.addProperty("message", Constants.ERROR_INCORRECT_USERNAME_TEXT);
            String response = obj.toString();
            return response;
        }

        // A friendship request already exists
        if (!Singleton.getInstance().existsFriendshipRequest(originUser, acceptedUser)){
            JsonObject obj = new JsonObject();
            obj.addProperty("code", Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST);
            obj.addProperty("message", Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST_TEXT);
            String response = obj.toString();
            return response;
        }

        // Remove the friendship request
        Singleton.getInstance().removeFriendshipRequest(originUser, acceptedUser);
        // Both become friends
        Singleton.getInstance().addFriend(originUser, acceptedUser);
        Singleton.getInstance().addFriend(acceptedUser, originUser);
        JsonObject obj = new JsonObject();
        obj.addProperty("code", Constants.ERROR_SUCCESSFUL);
        obj.addProperty("message", Constants.ERROR_SUCCESSFUL_TEXT);
        String response = obj.toString();
        return response;
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
