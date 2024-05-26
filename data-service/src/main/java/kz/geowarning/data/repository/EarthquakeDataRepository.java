package kz.geowarning.data.repository;

import kz.geowarning.data.entity.EarthquakeData;
import kz.geowarning.data.entity.dto.EarthquakeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EarthquakeDataRepository extends JpaRepository<EarthquakeData, Long> {

    @Query(nativeQuery = true, value = "SELECT data.earthquakedata.* " +
                "FROM data.earthquakedata " +
                "WHERE (:#{#eqDTO.latitude} = '0' OR " +
                "((CAST(:#{#eqDTO.latitude} AS DECIMAL) - 8.98) <= CAST(data.earthquakedata.latitude AS DECIMAL))) " +
                "AND (:#{#eqDTO.latitude} = '0' OR " +
                "(CAST(data.earthquakedata.latitude AS DECIMAL) <= " +
                "(CAST(:#{#eqDTO.latitude} AS DECIMAL) + 8.98))) " +
                "AND (:#{#eqDTO.longitude} = '0' OR " +
                "((CAST(:#{#eqDTO.longitude} AS DECIMAL) - 8.98) <= CAST(data.earthquakedata.longitude AS DECIMAL))) " +
                "AND (:#{#eqDTO.longitude} = '0' OR " +
                "(CAST(data.earthquakedata.longitude AS DECIMAL) <= " +
                "(CAST(:#{#eqDTO.longitude} AS DECIMAL) + 8.98))) " +
                "AND (:#{#eqDTO.regionId} = '0' OR " +
                "(CAST(:#{#eqDTO.regionId} AS VARCHAR) = data.earthquakedata.region_id)) " +
                "AND (:#{#eqDTO.depthFrom} = '0' OR " +
                "(CAST(data.earthquakedata.depth AS DECIMAL) >= CAST(:#{#eqDTO.depthFrom} AS DECIMAL))) " +
                "AND (:#{#eqDTO.depthTo} = '0' OR " +
                "(CAST(data.earthquakedata.depth AS DECIMAL) <= CAST(:#{#eqDTO.depthTo} AS DECIMAL))) " +
                "AND (:#{#eqDTO.magnitudeFrom} = '0' OR " +
                "(CAST(data.earthquakedata.mag AS DECIMAL) >= CAST(:#{#eqDTO.magnitudeFrom} AS DECIMAL))) " +
                "AND (:#{#eqDTO.magnitudeTo} = '0' OR " +
                "(CAST(data.earthquakedata.mag AS DECIMAL) <= CAST(:#{#eqDTO.magnitudeTo} AS DECIMAL))) " +
                "AND (:#{#eqDTO.dateFrom} = '0' OR " +
                "(CAST(:#{#eqDTO.dateFrom} AS TIMESTAMP) <= CAST(data.earthquakedata.time AS TIMESTAMP))) " +
                "AND (:#{#eqDTO.dateTo} = '0' OR " +
                "(CAST(:#{#eqDTO.dateTo} AS TIMESTAMP) >= CAST(data.earthquakedata.time AS TIMESTAMP))) ")
    List<EarthquakeData> findByFilter(@Param("eqDTO")EarthquakeDTO earthquakeDTO);

    Optional<EarthquakeData> findByUsgsId(String id);
}
