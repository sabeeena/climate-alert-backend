package kz.kazgeowarning.authgateway.security.oauth2;


import kz.kazgeowarning.authgateway.enums.Role;
import kz.kazgeowarning.authgateway.enums.UserLoginType;
import kz.kazgeowarning.authgateway.model.User;
import kz.kazgeowarning.authgateway.repository.UserRepository;
import kz.kazgeowarning.authgateway.security.oauth2.user.OAuth2UserInfo;
import kz.kazgeowarning.authgateway.security.oauth2.user.OAuth2UserInfoFactory;
import kz.kazgeowarning.authgateway.util.exception.OAuth2AuthenticationProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        if(StringUtils.isEmpty(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User user;
        if(userOptional.isPresent()) {
            user = userOptional.get();
            if(user.getLoginType() != null && !user.getLoginType().equals(UserLoginType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()))) {
                throw new OAuth2AuthenticationProcessingException("Похоже, вы зарегистрировались с учетной записью " +
                        user.getLoginType() + ". Пожалуйста, используйте вашу учетную запись  " + user.getLoginType() +
                        " для входа.");
            }
        } else {
            user = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        System.out.println("here din = " + oAuth2UserRequest);
        User user = new User();
        user.setLoginType(UserLoginType.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setFirstName(oAuth2UserInfo.getFirstName());
        user.setLastName(oAuth2UserInfo.getLastName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setRegisterDate(new Timestamp(new Date().getTime()));
        user.setRole(Role.ROLE_USER);
        user.setActive(true);
        user.setApproved(true);
        user.setStorageUrl(oAuth2UserInfo.getImageUrl());
        System.out.println("user = " + user);
        String result = RandomStringUtils.randomAlphabetic(64);
        user.setPassword(bCryptPasswordEncoder.encode(result));
        return userRepository.save(user);
    }

}
