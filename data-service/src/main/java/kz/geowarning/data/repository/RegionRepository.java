package kz.geowarning.data.repository;

import kz.geowarning.data.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, String> {
    @Query("SELECT r FROM Region r WHERE LOWER(r.name_eng) = LOWER(:name)")
    List<Region> findByNameEngCaseInsensitive(@Param("name") String name);
}
