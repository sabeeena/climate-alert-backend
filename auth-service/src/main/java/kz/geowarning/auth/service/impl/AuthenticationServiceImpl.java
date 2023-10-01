package kz.geowarning.auth.service.impl;

import kz.geowarning.auth.client.NotificationServiceClient;
import kz.geowarning.auth.dto.AuthenticationRequest;
import kz.geowarning.auth.dto.AuthenticationResponse;
import kz.geowarning.auth.dto.user.UserRegisterRequest;
import kz.geowarning.auth.entity.*;
import kz.geowarning.auth.repository.TokenRepository;
import kz.geowarning.auth.repository.UserRepository;
import kz.geowarning.auth.service.AuthenticationService;
import kz.geowarning.auth.service.JwtService;
import kz.geowarning.auth.service.OrganizationService;
import kz.geowarning.auth.service.RoleService;
import kz.geowarning.auth.validator.EmailValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private  NotificationServiceClient notificationServiceClient;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    private RoleService roleService;

    @Override
    public AuthenticationResponse registerUser(UserRegisterRequest request) {
        try {
            if (repository.existsByUsername(request.getUsername())) {
                return AuthenticationResponse.builder().status("fail").message("username exists").accessToken(null).build();
            }

            if (repository.existsByEmail(request.getEmail())) {
                return AuthenticationResponse.builder().status("fail").message("email exists").accessToken(null).build();
            }

            EmailValidator emailValidator = new EmailValidator();
            if (!emailValidator.validate(request.getEmail())) {
                return AuthenticationResponse.builder().status("fail").message("Неверный e-mail формат.").accessToken(null).build();
            }

            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .isEnabled(true)
                    .isDeleted(false)
                    .role(roleService.getRoleByRoleCode("USER"))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .middleName(request.getMiddleName())
                    .fullName(String.format("%s %s %s",
                            Optional.ofNullable(request.getLastName()).orElse(""),
                            Optional.ofNullable(request.getFirstName()).orElse(""),
                            Optional.ofNullable(request.getMiddleName()).orElse("")
                    ))
                    .birthDate(request.getBirthDate())
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .created(ZonedDateTime.now())
                    .isEmailVerified(false)
                    .isPhoneVerified(false)
                    .build();
            var savedUser = repository.save(user);
            notificationServiceClient.verifyEmail(request.getEmail());
//            var jwtToken = jwtService.generateToken(user);
            var jwtToken = "signupToken";
            saveUserToken(savedUser, jwtToken);
            return AuthenticationResponse.builder().status("success").message("user registered").accessToken(jwtToken).build();
        } catch (Exception exception) {
            return AuthenticationResponse.builder().status("fail - register exception").message(exception.getMessage()).accessToken(null).build();
        }
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            if (!(bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword()))) {
                return AuthenticationResponse.builder().status("fail").message("password incorrect").accessToken(null).build();
            }

            if(!user.isEmailVerified()){
                return AuthenticationResponse.builder().status("fail").message("user is not verified").accessToken(null).build();
            }

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            repository.findByEmail(request.getEmail()).get().getUsername(),
                            request.getPassword()
                    )
            );

            var jwtToken = jwtService.generateToken(user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .status("success")
                    .message("user authenticated")
                    .accessToken(jwtToken)
                    .build();
        } catch (Exception exception) {
            return AuthenticationResponse.builder().status("fail - authenticate exception").message(exception.getMessage()).accessToken(null).build();
        }
    }

    @Override
    public RedirectView confirmRegistration(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEmailVerified(true);
        repository.save(user);
        RedirectView redirectView = new RedirectView();
        String redirectUrl = "https://localhost/#/authentication/signin";
        redirectView.setUrl(redirectUrl);
        return redirectView;
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .token_type(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
