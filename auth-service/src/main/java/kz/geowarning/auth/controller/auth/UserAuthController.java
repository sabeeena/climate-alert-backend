package kz.geowarning.auth.controller.auth;

import kz.geowarning.auth.dto.AuthenticationRequest;
import kz.geowarning.auth.dto.AuthenticationResponse;
import kz.geowarning.auth.dto.user.UserRegisterRequest;
import kz.geowarning.auth.service.AuthenticationService;
import kz.geowarning.auth.util.RestConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping(RestConstants.BASE_REST + "/authorize")
@RequiredArgsConstructor
public class UserAuthController {
    private final AuthenticationService service;

    @Transactional(rollbackFor = Exception.class)
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

    @GetMapping("/confirm/{email}")
    public RedirectView confirmRegistration(@PathVariable("email") String email) {
        return ResponseEntity.ok(service.confirmRegistration(email)).getBody();
    }

}
