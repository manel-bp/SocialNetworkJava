package service;

import com.google.gson.Gson;
import model.Constants;
import model.User;
import repository.Singleton;

import java.lang.constant.Constable;
import java.util.ArrayList;

/**
 * The Service class will parse the information, check for possible errors and implement
 * the solution for each of the functionalities
 */
public class SnService {

    // Constructor
    public SnService(){}

    public String getErrorName(int error){
        switch (error){
            case Constants.ERROR_SUCCESSFUL:
                return Constants.ERROR_SUCCESSFUL_TEXT;
            case Constants.ERROR_USERNAME_TOO_SHORT:
                return Constants.ERROR_USERNAME_TOO_SHORT_TEXT;
            case Constants.ERROR_USERNAME_TOO_LONG:
                return Constants.ERROR_USERNAME_TOO_LONG_TEXT;
            case Constants.ERROR_USERNAME_REPEATED:
                return Constants.ERROR_USERNAME_REPEATED_TEXT;
            case Constants.ERROR_PASSWORD_TOO_SHORT:
                return Constants.ERROR_PASSWORD_TOO_SHORT_TEXT;
            case Constants.ERROR_PASSWORD_TOO_LONG:
                return Constants.ERROR_PASSWORD_TOO_LONG_TEXT;

            default:
                return "Non mapped error";

        }
    }

    public int signup(String body){
        // Parse information
        Gson g = new Gson();
        User user = g.fromJson(body, User.class);

        // Check possible errors
        // Length of username
        if (user.getUsername().length() > Constants.MAX_USERNAME_LENGTH){
            return Constants.ERROR_USERNAME_TOO_LONG;
        }else if (user.getUsername().length() < Constants.MIN_USERNAME_LENGTH){
            return Constants.ERROR_USERNAME_TOO_SHORT;
        }

        // Length of password
        if (user.getPassword().length() > Constants.MAX_PASSWORD_LENGTH){
            return Constants.ERROR_PASSWORD_TOO_LONG;
        }else if (user.getPassword().length() < Constants.MIN_PASSWORD_LENGTH){
            return Constants.ERROR_PASSWORD_TOO_SHORT;
        }

        // User already exists
        if (Singleton.getInstance().exists(user.getUsername())){
            return Constants.ERROR_USERNAME_REPEATED;
        }

        // If none of the errors was found, sign up the person
        Singleton.getInstance().addUser(user);
        return Constants.ERROR_SUCCESSFUL;
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
