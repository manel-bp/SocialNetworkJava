package model;

/**
 * In this class we will have the constants used for all the project
 */
public class Constants {
    // Constants:
    public static final int MIN_USERNAME_LENGTH = 5;
    public static final int MAX_USERNAME_LENGTH = 10;
    public static final int MIN_PASSWORD_LENGTH = 8;
    public static final int MAX_PASSWORD_LENGTH = 12;

    // Endpoints
    public static final int PORT = 8050;
    public static final String ENDPOINT_SIGNUP = "/signup";
    public static final String ENDPOINT_LOGIN = "/login";
    public static final String ENDPOINT_REQUEST_FRIENDSHIP = "/request-friendship";
    public static final String ENDPOINT_ACCEPT_FRIENDSHIP = "/accept-friendship";
    public static final String ENDPOINT_DECLINE_FRIENDSHIP = "/decline-friendship";
    public static final String ENDPOINT_LIST_FRIENDS = "/list-friends";

    public static final String LONG_ENDPOINT_SIGNUP = "http://localhost:" + PORT + ENDPOINT_SIGNUP;
    public static final String LONG_ENDPOINT_LOGIN = "http://localhost:" + PORT + ENDPOINT_LOGIN;
    public static final String LONG_ENDPOINT_REQUEST_FRIENDSHIP = "http://localhost:" + PORT + ENDPOINT_REQUEST_FRIENDSHIP;
    public static final String LONG_ENDPOINT_ACCEPT_FRIENDSHIP = "http://localhost:" + PORT + ENDPOINT_ACCEPT_FRIENDSHIP;
    public static final String LONG_ENDPOINT_DECLINE_FRIENDSHIP = "http://localhost:" + PORT + ENDPOINT_DECLINE_FRIENDSHIP;
    public static final String LONG_ENDPOINT_LIST_FRIENDS = "http://localhost:" + PORT + ENDPOINT_LIST_FRIENDS;

    // We are going to have an error code for each possible error
    public static final int ERROR_SUCCESSFUL = 0;
    public static final String ERROR_SUCCESSFUL_TEXT = "Request was processed successfully";

    //Signup errors
    public static final int ERROR_USERNAME_TOO_SHORT = 100;
    public static final String ERROR_USERNAME_TOO_SHORT_TEXT = "ERROR: Please, enter a username containing 5 to 10 characters";
    public static final int ERROR_USERNAME_TOO_LONG = 101;
    public static final String ERROR_USERNAME_TOO_LONG_TEXT = "ERROR: Please, enter a username containing 5 to 10 characters";
    public static final int ERROR_USERNAME_REPEATED = 102;
    public static final String ERROR_USERNAME_REPEATED_TEXT = "ERROR: Username is already registered";

    public static final int ERROR_PASSWORD_TOO_SHORT = 110;
    public static final String ERROR_PASSWORD_TOO_SHORT_TEXT = "ERROR: Please, enter a password between 8 and 12 alphanumeric characters";
    public static final int ERROR_PASSWORD_TOO_LONG = 111;
    public static final String ERROR_PASSWORD_TOO_LONG_TEXT = "ERROR: Please, enter a password between 8 and 12 alphanumeric characters";

    // Login errors
    public static final int ERROR_LOGIN = 200;
    public static final String ERROR_LOGIN_TEXT = "Error: Login failed. Check your username and password, or sign up";

    // Friendship requests related errors
    public static final int ERROR_INCORRECT_TOKEN = 300;
    public static final String ERROR_INCORRECT_TOKEN_TEXT = "ERROR: Invalid token.";
    public static final int ERROR_INCORRECT_USERNAME = 301;
    public static final String ERROR_INCORRECT_USERNAME_TEXT = "ERROR: Target username does not exist.";
    public static final int ERROR_INCORRECT_FRIENDSHIP_REQUEST = 302;
    public static final String ERROR_INCORRECT_FRIENDSHIP_REQUEST_TEXT = "ERROR: Target user and origin user are the same.";
    public static final int ERROR_FRIENDSHIP_REQUEST_REPEAT = 303;
    public static final String ERROR_FRIENDSHIP_REQUEST_REPEAT_TEXT = "ERROR: Friendship request is already waiting for an answer.";
    public static final int ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST = 304;
    public static final String ERROR_FRIENDSHIP_REQUEST_DOESNT_EXIST_TEXT = "ERROR: Friendship request does not exist";
    public static final int ERROR_FRIENDSHIP_REQUEST_TO_A_FRIEND = 305;
    public static final String ERROR_FRIENDSHIP_REQUEST_TO_A_FRIEND_TEXT = "ERROR: Friendship already exist between users";
}
