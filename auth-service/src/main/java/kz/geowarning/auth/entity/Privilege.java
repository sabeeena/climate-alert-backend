package kz.geowarning.auth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@Table(name = "privilege")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "privilege_seq")
    @SequenceGenerator(name = "privilege_seq", allocationSize = 1, sequenceName = "privilege_seq")
    private Long id;

    private String name;

    @Column(name = "privilege_code", unique = true)
    private String privilegeCode;

}
