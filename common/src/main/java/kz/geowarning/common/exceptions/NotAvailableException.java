package kz.geowarning.common.exceptions;

public class NotAvailableException extends Exception {

    public NotAvailableException(String message) {
        super(message);
    }

    public NotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

}
