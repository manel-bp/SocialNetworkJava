package model;

/**
 * This class creates a User type for all classes that need to use this type.
 */
public class User {
    // Global variables
    private String username;
    private String password;

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
}
