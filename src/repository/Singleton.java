package repository;

import model.Constants;
import model.User;

import java.util.ArrayList;

/**
 * Since it's not necessary to use a Database to store data, data will be stored using a Singleton structure.
 */
public class Singleton {
    // Global variables
    private static Singleton mInstance = null;
    private ArrayList<User> allUsers = new ArrayList<>();

    // Constructor
    public Singleton(){}

    // Static instance
    public static Singleton getInstance(){
        if(mInstance == null){
            mInstance = new Singleton();
        }
        return mInstance;
    }

    // Public functions / Interface
    public void addUser(User user){
        this.allUsers.add(user);
    }

    public ArrayList<User> listUsers(){
        return allUsers;
    }

    public boolean exists(String username){
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkLogin(String username, String password){
        for (User user : allUsers) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    public void setLoginToken(String username, String token){
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                user.setToken(token);
            }
        }
    }

    public String getLoginToken(String username){
        for (User user : allUsers) {
            if (user.getUsername().equals(username)) {
                return user.getToken();
            }
        }
        return null;
    }

    public String getUsernameFromToken(String token){
        for (User user : allUsers) {
            if (user.getToken() != null) {
                if (user.getToken().equals(token)) {
                    return user.getUsername();
                }
            }
        }
        return null;
    }

    public boolean existsFriendshipRequest(String originUsername, String targetUsername){
        // Look for the target user
        for (User user : allUsers) {
            if (user.getUsername().equals(targetUsername)) {
                // Check if origin user has a pending friendship request to the target user
                return user.existsFriendshipRequest(originUsername);
            }
        }
        return false;
    }

    public void addFriendshipRequest(String originUsername, String targetUsername){
        for (User user : this.allUsers) {
            if (user.getUsername().equals(targetUsername)) {
                user.appendFriendshipRequest(originUsername);
            }
        }
    }
}
