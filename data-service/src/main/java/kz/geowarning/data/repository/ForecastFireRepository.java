package kz.geowarning.data.repository;

import kz.geowarning.data.entity.ForecastFireData;
import kz.geowarning.data.entity.dto.ForecastDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForecastFireRepository extends JpaRepository<ForecastFireData, Long> {

    @Query(nativeQuery = true, value = "SELECT data.forecastfiredata.*, " +
            "(6371 * acos(cos(radians(CAST(:#{#fireDataDTO.latitude} AS DECIMAL))) * " +
            "cos(radians(CAST(s.latitude AS DECIMAL))) * " +
            "cos(radians(CAST(s.longitude AS DECIMAL)) - radians(CAST(:#{#fireDataDTO.longitude} AS DECIMAL))) + " +
            "sin(radians(CAST(:#{#fireDataDTO.latitude} AS DECIMAL))) * " +
            "sin(radians(CAST(s.latitude AS DECIMAL))))) AS distance " +
            "FROM data.forecastfiredata " +
            "JOIN data.stations s ON data.forecastfiredata.station_id = s.id " +
            "WHERE (:#{#fireDataDTO.latitude} = '0' OR " +
            "(CAST(:#{#fireDataDTO.latitude} AS DECIMAL) - 0.270) <= CAST(s.latitude AS DECIMAL)) " +
            "AND (:#{#fireDataDTO.latitude} = '0' OR " +
            "CAST(s.latitude AS DECIMAL) <= " +
            "(CAST(:#{#fireDataDTO.latitude} AS DECIMAL)) + 0.270) " +
            "AND (:#{#fireDataDTO.longitude} = '0' OR " +
            "(CAST(:#{#fireDataDTO.longitude} AS DECIMAL) - 0.270) <= CAST(s.longitude AS DECIMAL)) " +
            "AND (:#{#fireDataDTO.longitude} = '0' OR " +
            "CAST(s.longitude AS DECIMAL) <= " +
            "(CAST(:#{#fireDataDTO.longitude} AS DECIMAL)) + 0.270) " +
            "AND (:#{#fireDataDTO.regionId} = '0' OR " +
            "CAST(:#{#fireDataDTO.regionId} AS VARCHAR) = s.region_id) " +
            "AND (:#{#fireDataDTO.dateFrom} = '0' OR " +
            "data.forecastfiredata.time >= CAST(:#{#fireDataDTO.dateFrom} AS TIMESTAMP)) " +
            "AND (:#{#fireDataDTO.dateTo} = '0' OR " +
            "data.forecastfiredata.time <= CAST(:#{#fireDataDTO.dateTo} AS TIMESTAMP)) " +
            "AND (:#{#fireDataDTO.dangerLevelFrom} = '0' OR " +
            "(CAST(:#{#fireDataDTO.dangerLevelFrom} AS DECIMAL)) <= CAST(data.forecastfiredata.danger_level AS DECIMAL)) " +
            "AND (:#{#fireDataDTO.dangerLevelTo} = '0' OR " +
            "(CAST(:#{#fireDataDTO.dangerLevelTo} AS DECIMAL)) >= CAST(data.forecastfiredata.danger_level AS DECIMAL)) ")
    List<ForecastFireData> findByFilter(@Param("fireDataDTO") ForecastDTO fireDataDTO);
}
