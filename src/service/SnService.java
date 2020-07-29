package service;

import model.User;
import repository.Singleton;

import java.util.ArrayList;

/**
 * The Service class will parse the information, check for possible errors and implement
 * the solution for each of the functionalities
 */
public class SnService {

    // Constructor
    public SnService(){}

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
