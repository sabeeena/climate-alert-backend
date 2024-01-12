package kz.kazgeowarning.authgateway.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "_session", schema = "auth")
@NoArgsConstructor
@ToString
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "token")
    private String token;

    @Column(name = "token_create_date")
    private String tokenCreateDate;

    @Column(name = "token_expire_date")
    private String tokenExpireDate;

}
