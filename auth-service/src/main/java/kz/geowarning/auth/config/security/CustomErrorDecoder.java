package kz.geowarning.auth.config.security;

import feign.Response;
import feign.codec.ErrorDecoder;
import kz.geowarning.common.exceptions.BadRequestException;
import kz.geowarning.common.exceptions.NotAvailableException;
import kz.geowarning.common.exceptions.NotFoundException;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        switch (response.status()){
            case 400:
                return new BadRequestException("Bad Request");
            case 404:
                return new NotFoundException("Not Found");
            case 503:
                return new NotAvailableException("Unavailable");
            default:
                return new Exception("Exception Occurred");
        }
    }
}

