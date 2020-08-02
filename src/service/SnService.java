package service;

import com.google.gson.JsonObject;
import model.Constants;
import model.User;
import repository.Repository;

import java.util.UUID;

/**
 * The Service class will parse the information, check for possible errors and implement
 * the solution for each of the functionalities
 */
public class SnService {

    Repository repository;
    // Constructor
    public SnService(Repository repository){
        this.repository = repository;
    }

    public String signup(User user){
        // Check possible errors
        // Length of username
        if (user.getUsername().length() > Constants.MAX_USERNAME_LENGTH){
            return getResponse(Constants.ERROR_USERNAME_TOO_LONG, Constants.ERROR_USERNAME_TOO_LONG_TEXT);
        }else if (user.getUsername().length() < Constants.MIN_USERNAME_LENGTH){
            return getResponse(Constants.ERROR_USERNAME_TOO_SHORT, Constants.ERROR_USERNAME_TOO_SHORT_TEXT);
        }

        // Length of password
        if (user.getPassword().length() > Constants.MAX_PASSWORD_LENGTH){
            return getResponse(Constants.ERROR_PASSWORD_TOO_LONG, Constants.ERROR_PASSWORD_TOO_LONG_TEXT);
        }else if (user.getPassword().length() < Constants.MIN_PASSWORD_LENGTH){
            return getResponse(Constants.ERROR_PASSWORD_TOO_SHORT, Constants.ERROR_PASSWORD_TOO_SHORT_TEXT);
        }

        // User already exists
        if (repository.exists(user.getUsername())){
            return getResponse(Constants.ERROR_USERNAME_REPEATED, Constants.ERROR_USERNAME_REPEATED_TEXT);
        }

        // If none of the errors was found, sign up the person
        repository.addUser(user);
        return getResponse(Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);
    }

    public String login(User user){
        // If the user exists with the same username and password (login correct)
        if (repository.checkLogin(user.getUsername(), user.getPassword())){
            String token = getRandomToken();
            repository.setLoginToken(user.getUsername(), token);
            return getResponseData(Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT, token);
        }else{
            return getResponse(Constants.ERROR_LOGIN, Constants.ERROR_LOGIN_TEXT);
        }
    }

    public String requestFriendship(String token, String targetUser){
        // Check possible errors
        // The origin user exists (token is correct)
        String originUser = repository.getUsernameFromToken(token);
        if (originUser == null){
            return getResponse(Constants.ERROR_INCORRECT_TOKEN, Constants.ERROR_INCORRECT_TOKEN_TEXT);
        }

        // The target user exists
        if (!repository.exists(targetUser)){
            return getResponse(Constants.ERROR_INCORRECT_USERNAME, Constants.ERROR_INCORRECT_USERNAME_TEXT);
        }

        // Target user is not the same one as the origin user
        if (originUser.equals(targetUser)){
            return getResponse(Constants.ERROR_INCORRECT_FRIENDSHIP_REQUEST, Constants.ERROR_INCORRECT_FRIENDSHIP_REQUEST_TEXT);
        }

        // A pending friendship request does not exist yet
        if (repository.existsFriendshipRequest(originUser, targetUser)){
            return getResponse(Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT, Constants.ERROR_FRIENDSHIP_REQUEST_REPEAT_TEXT);
        }

        // Both users are not friends yet
        if (repository.existsFriendship(originUser, targetUser)){
            return getResponse(Constants.ERROR_FRIENDSHIP_REQUEST_TO_A_FRIEND, Constants.ERROR_FRIENDSHIP_REQUEST_TO_A_FRIEND_TEXT);
        }

        repository.addFriendshipRequest(originUser, targetUser);
        return getResponse(Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);
    }

    public String acceptFriendship(String token, String acceptedUser){
        // Check possible errors
        // The origin user exists (token is correct)
        String originUser = repository.getUsernameFromToken(token);
        if (originUser == null){
            return getResponse(Constants.ERROR_INCORRECT_TOKEN, Constants.ERROR_INCORRECT_TOKEN_TEXT);
        }

        // The accepted user exists
        if (!repository.exists(acceptedUser)){
            return getResponse(Constants.ERROR_INCORRECT_USERNAME, Constants.ERROR_INCORRECT_USERNAME_TEXT);
        }

        // A friendship request already exists
        if (!repository.doIHaveFriendshipRequest(originUser, acceptedUser)){
            return getResponse(Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST, Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST_TEXT);
        }

        // Remove the friendship request
        repository.removeFriendshipRequest(originUser, acceptedUser);
        // Both become friends
        repository.addFriend(originUser, acceptedUser);
        repository.addFriend(acceptedUser, originUser);

        return getResponse(Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);
    }

    public String declineFriendship(String token, String declinedUser){
        // Check possible errors
        // The origin user exists (token is correct)
        String originUser = repository.getUsernameFromToken(token);
        if (originUser == null){
            return getResponse(Constants.ERROR_INCORRECT_TOKEN, Constants.ERROR_INCORRECT_TOKEN_TEXT);
        }

        // The declined user exists
        if (!repository.exists(declinedUser)){
            return getResponse(Constants.ERROR_INCORRECT_USERNAME, Constants.ERROR_INCORRECT_USERNAME_TEXT);
        }

        // A friendship request already exists
        if (!repository.doIHaveFriendshipRequest(originUser, declinedUser)){
            return getResponse(Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST, Constants.ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST_TEXT);
        }

        // Remove the friendship request
        repository.removeFriendshipRequest(originUser, declinedUser);

        return getResponse(Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT);
    }

    public String listFriends(String token){
        // Check possible errors
        // The user exists (token is correct)
        String username = repository.getUsernameFromToken(token);
        if (username == null){
            return getResponse(Constants.ERROR_INCORRECT_TOKEN, Constants.ERROR_INCORRECT_TOKEN_TEXT);
        }

        // If no errors were found, respond with the list of friends
        String friends = repository.getFriendsList(username).toString();

        return getResponseData(Constants.ERROR_SUCCESSFUL, Constants.ERROR_SUCCESSFUL_TEXT, friends);
    }

    private String getResponse(int code, String message){
        JsonObject obj = new JsonObject();
        obj.addProperty(Constants.PROPERTY_CODE_NAME, code);
        obj.addProperty(Constants.PROPERTY_MESSAGE_NAME, message);

        return obj.toString();
    }

    private String getResponseData(int code, String message, String data){
        // Generate response
        JsonObject obj = new JsonObject();
        obj.addProperty(Constants.PROPERTY_CODE_NAME, code);
        obj.addProperty(Constants.PROPERTY_MESSAGE_NAME, message);
        obj.addProperty(Constants.PROPERTY_DATA_NAME, data);
        String response = obj.toString();

        return response;
    }

    private String getRandomToken(){
        // Generate Token
        return UUID.randomUUID().toString();
    }
}
