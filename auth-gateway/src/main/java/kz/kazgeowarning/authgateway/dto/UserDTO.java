package kz.kazgeowarning.authgateway.dto;

import kz.kazgeowarning.authgateway.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 91901774547107674L;

    private String signupToken;
    private String password;
    private String email;
    private String name;
    private String surname;
    private String middlename;


    public UserDTO(User user){
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.name = user.getFirstName();
        this.surname = user.getLastName();
        this.middlename = user.getMiddleName();
    }
}
