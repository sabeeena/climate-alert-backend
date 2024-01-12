package kz.kazgeowarning.authgateway.dto;


import kz.kazgeowarning.authgateway.enums.Role;
import kz.kazgeowarning.authgateway.enums.UserLoginType;
import lombok.*;

import javax.persistence.Column;
import java.util.Date;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String firstName;
    private String lastName;
    private String password;
    private String city;
    private Date birthDate;
    private String phoneNumber;
    private String email;
    private Role role;
    private UserLoginType loginType;

}
