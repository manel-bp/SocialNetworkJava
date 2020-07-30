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

    // We are going to have an error code for each possible error
    public static final int ERROR_SUCCESSFUL = 0;
    public static final String ERROR_SUCCESSFUL_TEXT = "Request was processed successfully";

    //Signup
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

    // Login
    public static final int ERROR_LOGIN = 200;
    public static final String ERROR_LOGIN_TEXT = "Error: Login failed. Check your username and password, or sign up";

    //
    public static final int ERROR_INCORRECT_TOKEN = 300;
    public static final String ERROR_INCORRECT_TOKEN_TEXT = "ERROR: Invalid token.";
    public static final int ERROR_INCORRECT_USERNAME = 301;
    public static final String ERROR_INCORRECT_USERNAME_TEXT = "ERROR: Username does not exist.";
    public static final int ERROR_INCORRECT_FRIENDSHIP_REQUEST = 302;
    public static final String ERROR_INCORRECT_FRIENDSHIP_REQUEST_TEXT = "ERROR: Target user and origin user are the same.";
    public static final int ERROR_FRIENDSHIP_REQUEST_REPEAT = 303;
    public static final String ERROR_FRIENDSHIP_REQUEST_REPEAT_TEXT = "ERROR: Friendship request is already waiting for an answer.";
}
