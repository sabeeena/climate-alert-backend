package kz.kazgeowarning.authgateway.util;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * Simple model for error handling
 * @author Zhetpisbayeva Saule
 * @version 1.0
 */
@Builder
@Getter
public class ErrorResponse implements Serializable {
    private String errorMsg;
    private String errorCode;
    private HttpStatus responseStatus;
    private int responseCode;
    private String details;

    public ErrorResponse(String errorMsg, String errorCode, HttpStatus responseStatus, int responseCode) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
        this.responseStatus = responseStatus;
        this.responseCode = responseCode;
    }

    public ErrorResponse(String errorMsg, String errorCode, HttpStatus responseStatus, int responseCode, String details) {
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
        this.responseStatus = responseStatus;
        this.responseCode = responseCode;
        this.details = details;
    }
}



