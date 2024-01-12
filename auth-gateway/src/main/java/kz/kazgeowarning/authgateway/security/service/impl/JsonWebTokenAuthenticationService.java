package kz.kazgeowarning.authgateway.security.service.impl;

import io.jsonwebtoken.*;
import kz.kazgeowarning.authgateway.model.Session;
import kz.kazgeowarning.authgateway.model.UserAuthentication;
import kz.kazgeowarning.authgateway.repository.SessionRepository;
import kz.kazgeowarning.authgateway.security.constants.SecurityConstants;
import kz.kazgeowarning.authgateway.security.service.TokenAuthenticationService;
import kz.kazgeowarning.authgateway.util.exception.ServiceException;
import kz.kazgeowarning.authgateway.util.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static kz.kazgeowarning.authgateway.security.constants.SecurityConstants.TOKEN_EXPIRE_TIME;


@Slf4j
@Service
@RequiredArgsConstructor
public class JsonWebTokenAuthenticationService implements TokenAuthenticationService {

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Value("${security.token.secret.key}")
    private String secretKey;

    private final UserDetailsService userDetailsService;
    private final UsersService usersService;
    private final SessionRepository sessionRepository;

    @Override
    @Transactional
    public Authentication authenticate(final HttpServletRequest request) {

        final String token = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
        final Jws<Claims> tokenData = parseToken(token);

        if (tokenData != null) {
            User user = getUserFromToken(tokenData);
            if (user != null) {
//                System.out.println("isTokenExpired(token) = " + isTokenExpired(token));
                if (!isTokenExpired(token)) {
                    return new UserAuthentication(user, this.usersService, token);
                } else {
                    throw new ServiceException("Token was expired", this.getClass().getName());
                }
            }
        }

//        else {
//            throw new ServiceException("Token was expired", this.getClass().getName());
//        }

        return null;
    }

    private Jws<Claims> parseToken(final String token) {
        if (token != null) {
            try {
                return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    private boolean isTokenExpired(String token) {

        System.out.println("token: " + token);
        Session session = sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(token);

        if (session != null) {
            if (session.getTokenExpireDate() == null || session.getTokenExpireDate().isEmpty()) {
                return false;
            }

//            System.out.println("exp date: " + session.getTokenExpireDate());

            Date tokenExpireDate = new Date(session.getTokenExpireDate());

//            System.out.println("original exp date: " + tokenExpireDate);

            Calendar now = Calendar.getInstance();
            Calendar tokenExpirationDateCalendar = Calendar.getInstance();
            tokenExpirationDateCalendar.setTime(tokenExpireDate);

//            System.out.println("expire date: " + DATE_FORMAT.format(tokenExpirationDateCalendar.getTime()));
//            System.out.println("now: " + DATE_FORMAT.format(now.getTime()));


            // TODO is not working properlu
            boolean isTokenExpired = tokenExpirationDateCalendar.before(now);
            System.out.println("tokenExp " + isTokenExpired);
            if (!isTokenExpired) {
                Calendar newExpireDate = Calendar.getInstance();
                long dateTimeInMillis = newExpireDate.getTimeInMillis();
                newExpireDate.add(Calendar.MINUTE, TOKEN_EXPIRE_TIME);
                Date date = new Date(dateTimeInMillis + (TOKEN_EXPIRE_TIME * 60 * 1000));
//                System.out.println("####newExpireDate:" + newExpireDate);
                String exDate = DATE_FORMAT.format(date);
//                System.out.println("####exDate:" + exDate);
                session.setTokenExpireDate(exDate);
                sessionRepository.save(session);
            }

            return isTokenExpired;

        } else {
            throw new ServiceException("Session does not exist", this.getClass().getName());
        }
    }

    private User getUserFromToken(final Jws<Claims> tokenData) {
        try {

            return (User) userDetailsService.loadUserByUsername(tokenData.getBody().get("username").toString());
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("User" + tokenData.getBody().get("username").toString() + " not found");
        }
    }

}
