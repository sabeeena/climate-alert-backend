package kz.kazgeowarning.authgateway.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import kz.kazgeowarning.authgateway.enums.Role;
import kz.kazgeowarning.authgateway.enums.UserLoginType;
import kz.kazgeowarning.authgateway.util.EnumTypePostgreSql;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name = "_users", schema = "auth")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TypeDefs({
        @TypeDef(name = "enum_postgre", typeClass = EnumTypePostgreSql.class)
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name="middle_name")
    private String middleName;
    @Column(name = "email")
    private String email;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @Type(type = "enum_postgre")
    private Role role;

    @Column(name = "password")
    private String password;

    @Column(name = "login_type")
    @Enumerated(EnumType.STRING)
    @Type(type = "enum_postgre")
    private UserLoginType loginType;
    @Transient
    private String storageUrl;

//    public String getStorageUrl() {
//        return Api.USER_IMAGE.getPath()  + getFileId();
//    }

    /** Активный ли пользователь */
    @Column(name = "active")
    private boolean active;

    @Column(name = "approved")
    private boolean approved;

    @Column(name = "signup_token")
    private String signupToken;

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "registration_date")
    private Timestamp registerDate;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "birth_date")
    private Date birthDate;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location locationId;

//    @Column(name = "city")
//    private String city;

    @Column(length = 2000)
    private String imageUrl;
    @Column(name = "notify_email")
    private boolean notifyEmail=false;
    @Column(name = "notify_sms")
    private boolean notifySms=false;

    @Column(name = "preferred_language")
    private String languageCode;

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }
}
