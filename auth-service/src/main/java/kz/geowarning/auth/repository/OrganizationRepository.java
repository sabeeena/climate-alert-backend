package kz.geowarning.auth.repository;

import kz.geowarning.auth.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findAll();

    Optional<Organization> findById(Long id);

    Optional<Organization> findByBin(String bin);

    boolean existsByBin(String bin);

    boolean existsByBinOrFullName(String bin, String fullName);

}
