package kz.geowarning.auth.service.impl;

import kz.geowarning.auth.dto.AuthenticationRequest;
import kz.geowarning.auth.dto.AuthenticationResponse;
import kz.geowarning.auth.dto.user.UserRegisterRequest;
import kz.geowarning.auth.entity.*;
import kz.geowarning.auth.repository.TokenRepository;
import kz.geowarning.auth.repository.UserRepository;
import kz.geowarning.auth.service.AuthenticationService;
import kz.geowarning.auth.service.JwtService;
import kz.geowarning.auth.service.RoleService;
import kz.geowarning.auth.validator.EmailValidator;
import kz.geowarning.common.api.NotificationClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final RoleService roleService;
    private final NotificationClient notificationClient;

    @Async
    public void verifyEmailAsync(String email) {
        notificationClient.verifyEmail(email);
    }

    @Override
    public AuthenticationResponse registerUser(UserRegisterRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            return AuthenticationResponse.builder()
                    .status("fail")
                    .message("Username exists")
                    .accessToken(null)
                    .build();
        }

        if (repository.existsByEmail(request.getEmail())) {
            return AuthenticationResponse.builder()
                    .status("fail")
                    .message("Email exists")
                    .accessToken(null)
                    .build();
        }

        EmailValidator emailValidator = new EmailValidator();
        if (!emailValidator.validate(request.getEmail())) {
            return AuthenticationResponse.builder()
                    .status("fail")
                    .message("Invalid email format")
                    .accessToken(null)
                    .build();
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
                                .trim()
                ))
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .phone(request.getPhone())
                .created(ZonedDateTime.now())
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .build();
        var savedUser = repository.save(user);
//        notificationClient.verifyEmail(request.getEmail());
        verifyEmailAsync(request.getEmail());
        var jwtToken = "signupToken";
        saveUserToken(savedUser, jwtToken);
        return AuthenticationResponse.builder()
                .status("success")
                .message("ok")
                .accessToken(jwtToken)
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        repository.findByEmail(request.getEmail()).get().getUsername(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .status("success")
                .message("user authenticated")
                .accessToken(jwtToken)
                .build();
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
