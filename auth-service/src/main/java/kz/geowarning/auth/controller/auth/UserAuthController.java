package kz.geowarning.auth.controller.auth;

import kz.geowarning.auth.dto.AuthenticationRequest;
import kz.geowarning.auth.dto.AuthenticationResponse;
import kz.geowarning.auth.dto.user.UserRegisterRequest;
import kz.geowarning.auth.service.AuthenticationService;
import kz.geowarning.auth.util.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(RestConstants.BASE_REST + "/authorize")
@RequiredArgsConstructor
public class UserAuthController {
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserRegisterRequest request
    ) {
        return ResponseEntity.ok(service.registerUser(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

}
