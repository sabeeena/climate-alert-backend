package kz.geowarning.auth.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Data
@NoArgsConstructor
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_seq")
    @SequenceGenerator(name = "role_seq", allocationSize = 1, sequenceName = "role_seq")
    private Long id;

    private String name;

    @Column(name = "role_code", unique = true)
    private String roleCode;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_privilege",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "privilege_id")
    )
    private Set<Privilege> privileges = new HashSet<>();

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPrivileges()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPrivilegeCode()))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.roleCode));
        return authorities;
    }
}
