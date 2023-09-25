package kz.geowarning.auth.dto.user;

import kz.geowarning.auth.entity.Organization;
import kz.geowarning.auth.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterRequest {
    private String username;
    private String password;
    private Long role;
    private Long organization;
    private String jobTitle;
    private String firstName;
    private String lastName;
    private String middleName;
    private String fullName;
    private LocalDate birthDate;
    private String email;
    private String phone;
}
