package kz.kazgeowarning.authgateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "registered_employee")
public class RegisteredEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String employeeEmail;
    private String firstName;
    private String lastName;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AdminEmployee admin;
}
