package kz.geowarning.auth.service;

import kz.geowarning.auth.dto.AuthenticationRequest;
import kz.geowarning.auth.dto.AuthenticationResponse;
import kz.geowarning.auth.dto.user.UserRegisterRequest;
import org.springframework.web.servlet.view.RedirectView;


public interface AuthenticationService {
    AuthenticationResponse registerUser(UserRegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);
    RedirectView confirmRegistration(String email);
}
