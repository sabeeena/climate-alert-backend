package kz.kazgeowarning.authgateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "_v_student_details", schema = "public")
public class ClientDetailDTO {
    @Id
    @Column(name = "client_id")
    Long clientId;
    @Column(name = "client_name")
    String clientName;
    @Column(name = "client_surname")
    String clientSurname;
    @Column(name = "aim_id")
    Long aimId;
    @Column(name = "aim_name")
    String aimName;
    @Column(name = "direction_id")
    Long directionId;
    @Column(name = "direction_name")
    String directionName;
    @Column(name = "phone_number")
    String phoneNumber;
    @Column(name = "email")
    String email;
    @Column(name = "org_id")
    Long orgId;
}
