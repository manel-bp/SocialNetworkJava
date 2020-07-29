package repository;

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
}
