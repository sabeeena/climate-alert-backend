package kz.kazgeowarning.authgateway.security.filter;

import kz.kazgeowarning.authgateway.security.service.TokenAuthenticationService;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationTokenFilter extends GenericFilterBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

    private final TokenAuthenticationService authenticationService;

    public AuthenticationTokenFilter(final TokenAuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
                         final FilterChain chain) throws IOException {
        try {
            final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
            final Authentication authentication = authenticationService.authenticate(httpServletRequest);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
//            SecurityContextHolder.getContext().setAuthentication(null);
        } catch (ServiceException e) {
            LOGGER.info("Exception: " + e.getMessage());
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
            HttpServletResponse resp =( HttpServletResponse) response;
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            LOGGER.info("Session timout: " + e.getMessage());
            ((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Session timout");
            HttpServletResponse resp =( HttpServletResponse) response;
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
