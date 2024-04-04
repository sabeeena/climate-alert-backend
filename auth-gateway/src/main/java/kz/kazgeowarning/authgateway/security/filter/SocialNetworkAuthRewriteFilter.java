package kz.kazgeowarning.authgateway.security.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class SocialNetworkAuthRewriteFilter extends OncePerRequestFilter {

    private static final String GET_PROTOCOL = "://.*";
    private static final String LINKED_IN = "oauth";
    private static final String HTTPS = "https";

    @Value("${base-url}")
    private String baseUrl;

    @Value("${server.port}")
    private int serverPort;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("SocialNetworkAuthRewriteFilter.doFilterInternal request.getRequestURL(): " + request.getRequestURL());

        if (request.getRequestURL().toString().contains(LINKED_IN)) {
            request = new LinkedInHttpServletRequestWrapper(request);
        }
        filterChain.doFilter(request, response);
    }

    public class LinkedInHttpServletRequestWrapper extends HttpServletRequestWrapper {

        public LinkedInHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getScheme() {
            baseUrl = "https://damoo.kz";
//            baseUrl = "http://localhost";
            log.info("LinkedInHttpServletRequestWrapper.getScheme baseUrl: " + baseUrl);
            return baseUrl.replaceFirst(GET_PROTOCOL, "");
        }

        @Override
        public int getServerPort() {
            log.info("LinkedInHttpServletRequestWrapper.getServerPort serverPort: " + serverPort);
            return HTTPS.equals(getScheme()) ? 443 : serverPort;
        }
    }
}

