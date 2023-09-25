package kz.geowarning.auth.service;

import kz.geowarning.auth.dto.AuthenticationRequest;
import kz.geowarning.auth.dto.AuthenticationResponse;
import kz.geowarning.auth.dto.user.UserRegisterRequest;


public interface AuthenticationService {
    AuthenticationResponse registerUser(UserRegisterRequest request);
    AuthenticationResponse authenticate(AuthenticationRequest request);




}
