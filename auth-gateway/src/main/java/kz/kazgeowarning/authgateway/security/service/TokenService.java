package kz.kazgeowarning.authgateway.security.service;


import kz.kazgeowarning.authgateway.dto.TokenDTO;
import kz.kazgeowarning.authgateway.util.error.InternalException;

import javax.servlet.http.HttpServletRequest;

public interface TokenService {
    TokenDTO getToken(String username, String password);
    String deactivateToken(HttpServletRequest request) throws InternalException;
}
