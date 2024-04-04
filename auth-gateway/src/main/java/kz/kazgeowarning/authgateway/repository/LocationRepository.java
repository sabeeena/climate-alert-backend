package kz.kazgeowarning.authgateway.repository;

import kz.kazgeowarning.authgateway.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("select l from Location l where lower(l.name) = lower(:name)")
    Optional<Location> getByName(String name);

}
