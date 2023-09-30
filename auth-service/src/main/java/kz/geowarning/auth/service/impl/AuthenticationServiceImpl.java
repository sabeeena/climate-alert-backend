package kz.geowarning.auth.service.impl;

import kz.geowarning.auth.dto.AuthenticationRequest;
import kz.geowarning.auth.dto.AuthenticationResponse;
import kz.geowarning.auth.dto.user.UserRegisterRequest;
import kz.geowarning.auth.entity.*;
import kz.geowarning.auth.repository.TokenRepository;
import kz.geowarning.auth.repository.UserRepository;
import kz.geowarning.auth.service.AuthenticationService;
import kz.geowarning.auth.service.JwtService;
import kz.geowarning.auth.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    @Autowired
    OrganizationService organizationService;

    @Override
    public AuthenticationResponse registerUser(UserRegisterRequest request) {
        if (repository.existsByUsername(request.getUsername())) {
            return AuthenticationResponse.builder()
                    .status("fail")
                    .message("username exists")
                    .accessToken(null)
                    .build();
        }

        if (repository.existsByEmail(request.getEmail())) {
            return AuthenticationResponse.builder()
                    .status("fail")
                    .message("email exists")
                    .accessToken(null)
                    .build();
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .isEnabled(true)
                .isDeleted(false)
                .role(new Role().setId(request.getRole()))
                .organization(organizationService.getOrganizationById(request.getOrganization()))
             //   .organization(new Organization(request.getOrganization()).setId(request.getOrganization()))
                .jobTitle(request.getJobTitle())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .middleName(request.getMiddleName())
                .fullName(request.getFullName())
                .birthDate(request.getBirthDate())
                .email(request.getEmail())
                .phone(request.getPhone())
                .created(ZonedDateTime.now())
                .modified(ZonedDateTime.now())
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .build();
        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
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
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .build();
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
