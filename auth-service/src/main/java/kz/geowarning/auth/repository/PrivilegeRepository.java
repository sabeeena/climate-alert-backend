package kz.geowarning.auth.repository;

import kz.geowarning.auth.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    boolean existsByPrivilegeCode(String code);

    List<Privilege> findAll();

}
