package kz.kazgeowarning.authgateway.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kz.kazgeowarning.authgateway.model.RegisteredEmployee;
import kz.kazgeowarning.authgateway.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AdminEmployeeDTO {
    private String adminEmail;

    private List<User> employees;
}
