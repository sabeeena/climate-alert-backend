package kz.geowarning.common.exceptions;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class GeneralException extends Exception {

    private Map<String, String> errors;

    public GeneralException(String message) {
        super(message);
    }

    public GeneralException(String message, String errorData) {
        super(message);
        this.errors = Collections.singletonMap("err", errorData);
    }

    public GeneralException(String message, Throwable cause) {
        super(message, cause);
    }
}