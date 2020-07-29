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
}
