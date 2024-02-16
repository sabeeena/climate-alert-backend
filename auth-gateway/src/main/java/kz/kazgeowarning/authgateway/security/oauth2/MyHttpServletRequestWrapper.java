package kz.kazgeowarning.authgateway.security.oauth2;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@Slf4j
public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public final String redirectUrl;
    private static final String GET_PROTOCOL = "://.*";
    private static final String HTTPS = "http";

    public MyHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.redirectUrl = "http://localhost:4200" + request.getRequestURI();
        log.info("this.redirectUrl = " + this.redirectUrl);
    }

    @Override
    public StringBuffer getRequestURL() {
        return new StringBuffer(redirectUrl);
    }
}

