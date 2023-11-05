package kz.geowarning.common.exceptions;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public class CommonException extends Exception {

    private Map<String, String> errors;

    public CommonException(String message) {
        super(message);
    }

    public CommonException(String message, String errorData) {
        super(message);
        this.errors = Collections.singletonMap("err", errorData);
    }

    public CommonException(String message, Throwable cause) {
        super(message, cause);
    }
}