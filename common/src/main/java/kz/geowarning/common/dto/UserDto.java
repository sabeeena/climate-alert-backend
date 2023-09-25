package kz.geowarning.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {

    private Long id;
    private String username;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private boolean isEnabled;
    private Long roleId;
    private Long organizationId;
    private String jobTitle;
    private String firstName;
    private String lastName;
    private String middleName;
    private String fullName;
    private LocalDate birthDate;
    private String email;
    private String phone;

    public UserDto(Long id, String username, String fullName, String email, String phone, Long organizationId, String jobTitle, LocalDate birthDate){
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.organizationId = organizationId;
        this.jobTitle = jobTitle;
        this.birthDate = birthDate;
    }

}
