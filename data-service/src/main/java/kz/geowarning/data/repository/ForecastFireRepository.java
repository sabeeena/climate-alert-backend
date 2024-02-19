package kz.geowarning.data.repository;

import kz.geowarning.data.entity.ForecastFireData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ForecastFireRepository extends JpaRepository<ForecastFireData, Long> {
}
