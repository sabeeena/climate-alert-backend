package kz.geowarning.data.repository;

import kz.geowarning.data.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StationsRepository extends JpaRepository<Station, String> {

    @Query("SELECT s FROM Station s")
    List<Station> getAllStations();
}
