package kz.kazgeowarning.authgateway.security.service;

import org.hibernate.service.spi.ServiceException;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface TokenAuthenticationService {
    Authentication authenticate(HttpServletRequest request) throws ServiceException;
}
