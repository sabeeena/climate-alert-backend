package kz.kazgeowarning.authgateway.util.exception;

import kz.kazgeowarning.authgateway.util.BaseException;
import kz.kazgeowarning.authgateway.util.ErrorMessageConstants;
import kz.kazgeowarning.authgateway.util.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Resource not found exception
 * @author Zhetpisbayeva Saule
 * @version 1.0
 */
@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends BaseException {

    public ResourceNotFoundException(final Class<?> type, final Object identifier) {
        super(new ErrorResponse(String.format(ErrorMessageConstants.NOT_FOUND.MESSAGE, type.getSimpleName(), identifier),
                ErrorMessageConstants.NOT_FOUND.ERROR_CODE, HttpStatus.NO_CONTENT, HttpStatus.NO_CONTENT.value()));

    }
}
