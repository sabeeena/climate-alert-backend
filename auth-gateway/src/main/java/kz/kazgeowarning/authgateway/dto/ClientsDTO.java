package kz.kazgeowarning.authgateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import kz.kazgeowarning.authgateway.enums.Role;
import kz.kazgeowarning.authgateway.enums.UserLoginType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientsDTO {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("email")
    private String email;

    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("middleName")
    private String middleName;

    @JsonProperty("role")
    private Role role;

    @JsonProperty("password")
    private String password;

    @JsonProperty("loginType")
    private UserLoginType loginType;

    @JsonProperty("registerDate")
    private Timestamp registerDate;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("birthDate")
    private Date birthDate;

//    @JsonProperty("city")
//    private String city;

    @JsonProperty("location_name")
    private String locationName;

    @JsonProperty("location_latitude")
    private String latitude;

    @JsonProperty("location_longitude")
    private String longitude;

    @JsonProperty("imageUrl")
    private String imageUrl;
    @JsonProperty("notifyEmail")
    private boolean notifyEmail;
    @JsonProperty("notifySms")
    private boolean notifySms;
    @JsonProperty("preferred_language")
    private String languageCode;

}
