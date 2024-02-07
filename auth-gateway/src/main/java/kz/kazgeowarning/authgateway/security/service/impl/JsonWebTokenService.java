package kz.kazgeowarning.authgateway.security.service.impl;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kz.kazgeowarning.authgateway.dto.TokenDTO;
import kz.kazgeowarning.authgateway.model.Session;
import kz.kazgeowarning.authgateway.model.User;
import kz.kazgeowarning.authgateway.repository.SessionRepository;
import kz.kazgeowarning.authgateway.security.constants.SecurityConstants;
import kz.kazgeowarning.authgateway.security.service.TokenService;
import kz.kazgeowarning.authgateway.util.error.InternalException;
import kz.kazgeowarning.authgateway.util.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static kz.kazgeowarning.authgateway.security.constants.SecurityConstants.TOKEN_EXPIRE_TIME;


@Service
@RequiredArgsConstructor
public class JsonWebTokenService implements TokenService {

    private final DateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Value("${security.token.secret.key}")
    private String tokenKey;

    private final BasicUserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UsersService usersService;
    private final SessionRepository sessionRepository;

    @Override
    public String deactivateToken(HttpServletRequest request) throws InternalException {
        final String token = request.getHeader(SecurityConstants.AUTH_HEADER_NAME);
        if (token != null) {
            Session session = this.sessionRepository.findFirstByTokenOrderByTokenCreateDateDesc(token);
            if (session != null) {
                Calendar now = Calendar.getInstance();
                session.setTokenExpireDate(DATE_FORMAT.format(now.getTime()));
                this.sessionRepository.save(session);
            }
        }
        return "Logged out successfully";
    }

    @Override
    public TokenDTO getToken(final String username, final String password) {

        if (username == null || password == null) {
            throw new ServiceException("Idn or password was not provided", this.getClass().getName());
        }

        org.springframework.security.core.userdetails.User userDetails =
                (org.springframework.security.core.userdetails.User) userDetailsService.loadUserByUsername(username);

        final User user = usersService.findUserByUsername(userDetails.getUsername());

        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            Calendar calendar = Calendar.getInstance();
            String createDate = DATE_FORMAT.format(calendar.getTime());

            Map<String, Object> tokenData = new HashMap<>();
            tokenData.put("clientType", "user");
            tokenData.put("userId", user.getId());
            tokenData.put("username", user.getEmail());
            tokenData.put("createDate", createDate);

            JwtBuilder jwtBuilder = Jwts.builder();
            jwtBuilder.setClaims(tokenData);
            String token = jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenKey).compact();

            Session session = new Session();
            session.setToken(token);
            session.setUserId(user.getId());
            session.setTokenCreateDate(createDate);
            calendar.add(Calendar.MINUTE, TOKEN_EXPIRE_TIME);
            session.setTokenExpireDate(DATE_FORMAT.format(calendar.getTime()));
            this.sessionRepository.save(session);

            final TokenDTO tokenResponse = new TokenDTO();
            tokenResponse.setUser(user);
            tokenResponse.setToken(token);

            return tokenResponse;
        } else {
            throw new ServiceException("Token generation was failed during authentication", this.getClass().getName());
        }
    }

}
