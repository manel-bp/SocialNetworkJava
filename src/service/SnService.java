package service;

import com.google.gson.Gson;
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

    public void signup(String body){
        System.out.println(body);
        Gson g = new Gson();
        User p = g.fromJson(body, User.class);
        System.out.println(p.getUsername());
        System.out.println(p.getPassword());
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
