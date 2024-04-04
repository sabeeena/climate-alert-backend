package kz.kazgeowarning.authgateway.util;

/**
 * Interface for getting error messages with codes
 * @author Zhetpisbayeva Saule
 * @version 1.0
 */
public class ErrorMessageConstants {

    public interface METHOD_ARGUMENT_NOT_VALID {
        String ERROR_CODE = "INVALID_ARGUMENT";
        String MESSAGE = "Method arguments are invalid";
    }

    public interface INTERNAL_SERVER_ERROR {
        String ERROR_CODE = "INTERNAL_SERVER_ERROR";
        String MESSAGE = "Server system error";
    }

    public interface INVALID_TOKEN {
        String ERROR_CODE = "UNAUTHORIZED";
        String MESSAGE = "Invalid Access Token";
    }

    public interface TOKEN_NOT_FOUND {
        String ERROR_CODE = "UNAUTHORIZED";
        String MESSAGE = "Token not found.";
    }

    public interface INVALID_AUTH_ARGS {
        String ERROR_CODE = "UNAUTHORIZED";
        String MESSAGE = "Invalid login or password";
    }

    public interface USER_NOT_FOUND {
        String ERROR_CODE = "USER_NOT_FOUND";
        String MESSAGE = "User not exist";
    }

    public interface EMAIL_FIELD_NOT_FOUND {
        String ERROR_CODE = "EMAIL_FIELD_NOT_FOUND";
        String MESSAGE = "No such field with name 'email'";
    }

    public interface USER_EXIST {
        String ERROR_CODE = "USER_EXIST";
        String MESSAGE = "User already exist";
    }

    public interface USERNAME_EXIST {
        String ERROR_CODE = "USERNAME_EXIST";
        String MESSAGE = "An account with the given email already exists.";
    }

    public interface NOT_FOUND {
        String ERROR_CODE = "NOT_FOUND";
        String MESSAGE = "Requested resource of type [%s] with identifier [%s] not found";
    }

    public interface REGISTRATION_ERROR {
        String ERROR_CODE = "REGISTRATION_ERROR";
        String MESSAGE = "Error occurred: %s";
    }

    public interface AUTHORIZATION_ERROR {
        String ERROR_CODE = "AUTHORIZATION_ERROR";
        String MESSAGE = "Error occurred: %s";
    }

    public interface DEACTIVATION_ERROR {
        String ERROR_CODE = "DEACTIVATION_ERROR";
        String MESSAGE = "Error occurred: %s";
    }

    public interface UPDATE_ERROR {
        String ERROR_CODE = "UPDATE_ERROR";
        String MESSAGE = "Error occurred: %s";
    }

    public interface DELETE_ERROR {
        String ERROR_CODE = "DELETE_ERROR";
        String MESSAGE = "Error occurred: %s";
    }

    public interface UPLOAD_ERROR {
        String ERROR_CODE = "UPLOAD_ERROR";
        String MESSAGE = "Could not upload the file: %s ! Message: %s";
    }

    public interface DOWNLOAD_ERROR {
        String ERROR_CODE = "DOWNLOAD_ERROR";
        String MESSAGE = "Cannot download file: %s";
    }

    public interface CONTENT_NOT_FOUND {
        String ERROR_CODE = "CONTENT_NOT_FOUND";
        String MESSAGE = "Content does not find";
    }

    public interface INVALID_ARGUMENT {
        String ERROR_CODE = "INVALID_ARGUMENT";
        String MESSAGE = "Occurs an invalid parameter";
    }

    public interface PASSWORD_RESET_REQUIRED {
        String ERROR_CODE = "PASSWORD_RESET_REQUIRED";
        String MESSAGE = "A password reset is required";
    }

    public interface CANT_READ_FILE_EXCEPTION {
        String ERROR_CODE = "CANT_READ_FILE_EXCEPTION";
        String MESSAGE = "Can't read file";
    }

}
