package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates a User type for all classes that need to use this type.
 */
public class User {
    // Global variables
    private String username;
    private String password;
    private String token;
    private ArrayList<String> friends = new ArrayList<>();
    private ArrayList<String> friendshipRequests = new ArrayList<>();

    // Constructor including parameters
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    // Public functions, Interface
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void appendFriendshipRequest(String username){
        this.friendshipRequests.add(username);
    }

    public boolean existsFriendshipRequest(String originUser){
        return this.friendshipRequests.contains(originUser);
    }

    public boolean existsFriendship(String originUser){
        return this.friends.contains(originUser);
    }

    public void removeFriendship(String removedUser){
        this.friendshipRequests.remove(removedUser);
    }

    public void addFriendship(String newFriend){
        this.friends.add(newFriend);
    }

    public ArrayList<String> getFriendshipRequestsList(){
        return this.friendshipRequests;
    }

    public ArrayList<String> getFriendsList(){
        return this.friends;
    }
}
