package kz.kazgeowarning.authgateway.util;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Zhetpisbayeva Saule
 * @version 1.0
 */
@Data
@AllArgsConstructor
public abstract class BaseException extends RuntimeException {
    private ErrorResponse errorResponse;

}
