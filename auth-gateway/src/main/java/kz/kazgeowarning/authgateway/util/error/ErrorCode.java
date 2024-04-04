package kz.kazgeowarning.authgateway.util.error;

public class ErrorCode {

    // HashMap params;
    public enum ErrorCodes {
        SYSTEM_ERROR,
        AUTH_ERROR,
        PASSWORDS_NOT_MATCH,
        SESSION_TIMOUT,
        CONNECTION_ERROR,
        REQUEST_ERROR,
        RESPONSE_ERROR,
        INVALID_EMAIL_FORMAT,
        IDN_EXIST,
        EMAIL_EXIST,
        INVALID_TOKEN,
        NOT_FOUND
    }

    public static String toString(ErrorCodes errorRef) {
        return errorRef.toString();
    }
}
