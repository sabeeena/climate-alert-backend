package kz.geowarning.data.repository;

import kz.geowarning.data.entity.EarthquakeData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EarthquakeDataRepository extends JpaRepository<EarthquakeData, Long> {
}
