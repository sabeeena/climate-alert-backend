package kz.geowarning.auth.repository;

import kz.geowarning.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAll();

    Optional<Role> findById(Long id);

    boolean existsById(Long id);

    boolean existsByRoleCode(String roleCode);

    Optional<Role> getRoleByRoleCode(String roleCode);

}
