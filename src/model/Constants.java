package model;

/**
 * In this class we will have the constants used for all the project
 */
public class Constants {
    // We are going to have an error code for each possible error
    public static final int ERROR_SUCCESSFUL = 0;
    public static final String ERROR_SUCCESSFUL_TEXT = "Request was processed successfully";

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
    
}
