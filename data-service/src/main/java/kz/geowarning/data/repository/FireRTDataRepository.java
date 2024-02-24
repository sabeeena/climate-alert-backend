package kz.geowarning.data.repository;

import kz.geowarning.data.entity.FireRTData;
import kz.geowarning.data.entity.dto.FireDataDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface FireRTDataRepository extends JpaRepository<FireRTData, Long> {

    List<FireRTData> findAllByAcqDate(Date date);

    @Query(value="SELECT data.firertdata.* from data.firertdata, public.fire_real_time_report, public.editor\n" +
            "         WHERE EXTRACT(YEAR FROM acq_date) =:year AND EXTRACT(month FROM acq_date) =:month\n" +
            "and data.firertdata.id = public.fire_real_time_report.firertdata_id\n" +
            "           and public.fire_real_time_report.id = public.editor.report_id\n" +
            "           and email =:email", nativeQuery = true)
    List<FireRTData> findByYearAndMonthAndEmail(Integer year, Integer month, String email);

    @Query(nativeQuery = true, value = "SELECT data.firertdata.* from data.firertdata\n" +
            "                     WHERE EXTRACT(YEAR FROM acq_date) =:year AND EXTRACT(month FROM acq_date) =:month")
    List<FireRTData> findByYearAndMonth(Integer year, Integer month);

    @Query(nativeQuery = true, value = "SELECT data.firertdata.* FROM data.firertdata " +
            "WHERE (:#{#fireDataDTO.latitude} = '0' OR " +
            "(CAST(:#{#fireDataDTO.latitude} AS DECIMAL) - 0.270) <= CAST(data.firertdata.latitude AS DECIMAL)) " +
            "AND (:#{#fireDataDTO.latitude} = '0' OR " +
            "CAST(data.firertdata.latitude AS DECIMAL) <= " +
            "CAST(:#{#fireDataDTO.latitude} AS DECIMAL)) " +
            "AND (:#{#fireDataDTO.longitude} = '0' OR " +
            "(CAST(:#{#fireDataDTO.longitude} AS DECIMAL) - 0.270) <= CAST(data.firertdata.longitude AS DECIMAL)) " +
            "AND (:#{#fireDataDTO.longitude} = '0' OR " +
            "CAST(data.firertdata.longitude AS DECIMAL) <= " +
            "CAST(:#{#fireDataDTO.longitude} AS DECIMAL)) " +
            "AND (:#{#fireDataDTO.regionId} = '0' OR " +
            "CAST(:#{#fireDataDTO.regionId} AS VARCHAR) = data.firertdata.region_id) " +
            "AND (:#{#fireDataDTO.dateFrom} = '0' OR " +
            "data.firertdata.acq_date >= CAST(:#{#fireDataDTO.dateFrom} AS DATE)) " +
            "AND (:#{#fireDataDTO.dateTo} = '0' OR " +
            "data.firertdata.acq_date <= CAST(:#{#fireDataDTO.dateTo} AS DATE)) " +
            "AND (:#{#fireDataDTO.timeFrom} = '0' OR " +
            "CAST(CONCAT(data.firertdata.acq_date, ' ', data.firertdata.acq_time) AS TIMESTAMP) >= CAST(CONCAT(CAST(:#{#fireDataDTO.dateFrom} AS DATE), ' ', CAST(:#{#fireDataDTO.timeFrom} AS TIME)) AS TIMESTAMP)) " +
            "AND (:#{#fireDataDTO.timeTo} = '0' OR " +
            "CAST(CONCAT(data.firertdata.acq_date, ' ', data.firertdata.acq_time) AS TIMESTAMP) <= CAST(CONCAT(CAST(:#{#fireDataDTO.dateTo} AS DATE), ' ', CAST(:#{#fireDataDTO.timeTo} AS TIME)) AS TIMESTAMP)) ")
    List<FireRTData> findByFilter(@Param("fireDataDTO") FireDataDTO fireDataDTO);
}
