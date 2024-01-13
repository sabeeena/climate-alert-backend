package kz.kazgeowarning.authgateway.security.filter;

import kz.kazgeowarning.authgateway.security.oauth2.MyHttpServletRequestWrapper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyCustomFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (request.getRequestURL().toString().contains("oauth2")) {
            request = new MyHttpServletRequestWrapper(request);
        }

        chain.doFilter(request, response);
    }

}
