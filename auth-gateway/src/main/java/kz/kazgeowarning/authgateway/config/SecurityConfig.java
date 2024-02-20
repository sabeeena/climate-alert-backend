package kz.kazgeowarning.authgateway.config;

import kz.kazgeowarning.authgateway.security.filter.AuthenticationTokenFilter;
import kz.kazgeowarning.authgateway.security.filter.MyCustomFilter;
import kz.kazgeowarning.authgateway.security.filter.SocialNetworkAuthRewriteFilter;
import kz.kazgeowarning.authgateway.security.oauth2.CustomOAuth2UserService;
import kz.kazgeowarning.authgateway.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import kz.kazgeowarning.authgateway.security.oauth2.OAuth2AuthenticationFailureHandler;
import kz.kazgeowarning.authgateway.security.oauth2.OAuth2AuthenticationSuccessHandler;
import kz.kazgeowarning.authgateway.security.service.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableAutoConfiguration
@EnableConfigurationProperties
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticationService tokenAuthenticationService;

    @Autowired
    private CustomOAuth2UserService customOAuth2UserService;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    protected SecurityConfig(final TokenAuthenticationService tokenAuthenticationService){
        super();
        this.tokenAuthenticationService = tokenAuthenticationService;
    }

    @Bean
    public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new HttpCookieOAuth2AuthorizationRequestRepository();
    }

    public SocialNetworkAuthRewriteFilter linkedInRewriteFilter() {
        return new SocialNetworkAuthRewriteFilter();
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/oauth2/**",
                        "/api/news",
                        "/api/news/"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new MyCustomFilter(), OAuth2LoginAuthenticationFilter.class)
                .addFilterBefore(linkedInRewriteFilter(), OAuth2AuthorizationRequestRedirectFilter.class)
                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorize")
                        .authorizationRequestRepository(cookieAuthorizationRequestRepository())
                        .and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/callback/*")
                        .and()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                        .and()
                    .successHandler(oAuth2AuthenticationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .addFilterBefore(new AuthenticationTokenFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable();

    }
}
