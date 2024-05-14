package kz.geowarning.data.repository;

import kz.geowarning.data.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StationsRepository extends JpaRepository<Station, String> {

    @Query("SELECT s FROM Station s")
    List<Station> getAllStations();

    @Query("SELECT s FROM Station s WHERE s.id = :id")
    Optional<Station> getStationById(String id);
}
