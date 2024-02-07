package kz.kazgeowarning.authgateway.security.oauth2;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import kz.kazgeowarning.authgateway.config.AppProperties;
import kz.kazgeowarning.authgateway.dto.TokenDTO;
import kz.kazgeowarning.authgateway.model.Session;
import kz.kazgeowarning.authgateway.model.User;
import kz.kazgeowarning.authgateway.repository.SessionRepository;
import kz.kazgeowarning.authgateway.repository.UserRepository;
import kz.kazgeowarning.authgateway.security.constants.SecurityConstants;
import kz.kazgeowarning.authgateway.util.CookieUtils;
import kz.kazgeowarning.authgateway.util.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    @Value("${security.token.secret.key}")
    private String tokenKey;

    private final AppProperties appProperties;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository usersRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }

        targetUrl = targetUrl.replace("/home", "/#/home");
        log.info("---------------------------");
        log.info("TARGET URL  :: " + targetUrl);
        log.info("---------------------------");
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);

        if(redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }

        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        TokenDTO tokenDto = createTokenViaSocial((UserPrincipal) authentication.getPrincipal());

        return UriComponentsBuilder.fromUriString(targetUrl)
                .queryParam("token", tokenDto.getToken())
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);
        return appProperties.getOauth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    if(authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort()) {
                        return true;
                    }
                    return false;
                });
    }


    public TokenDTO createTokenViaSocial(UserPrincipal userPrincipal){
        User user = usersRepository.getById(userPrincipal.getId());
        TimeZone asiaSingapore = TimeZone.getTimeZone("Asia/Almaty");
        Calendar calendar = Calendar.getInstance(asiaSingapore);
        String createDate = dateFormat.format(calendar.getTime());

        Map<String, Object> tokenData = new HashMap<>();
        tokenData.put("clientType", "user");
        tokenData.put("userId", user.getId());
        tokenData.put("username", user.getEmail());
        tokenData.put("createDate", createDate);

        System.out.println("tokenData = " + tokenData);
        System.out.println("user = " + user);

        JwtBuilder jwtBuilder = Jwts.builder();
        jwtBuilder.setClaims(tokenData);
        String token = jwtBuilder.signWith(SignatureAlgorithm.HS512, tokenKey).compact();

        Session session = new Session();
        session.setToken(token);
        session.setUserId(user.getId());
        session.setTokenCreateDate(createDate);
        calendar.add(Calendar.MINUTE, SecurityConstants.TOKEN_EXPIRE_TIME);
        session.setTokenExpireDate(dateFormat.format(calendar.getTime()));

        System.out.println("session = " + session);
        this.sessionRepository.save(session);

        final TokenDTO tokenResponse = new TokenDTO();
        tokenResponse.setUser(user);
        tokenResponse.setToken(token);

        return tokenResponse;

    }
}
