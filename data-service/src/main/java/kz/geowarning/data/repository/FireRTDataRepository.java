package kz.geowarning.data.repository;

import kz.geowarning.data.entity.FireRTData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface FireRTDataRepository extends JpaRepository<FireRTData, Long> {

    List<FireRTData> findAllByAcqDate(Date date);

    @Query(value="SELECT * FROM data.firertdata\n" +
            "WHERE EXTRACT(YEAR FROM acq_date) =:year AND EXTRACT(month FROM acq_date) =:month", nativeQuery = true)
    List<FireRTData> findByYearAndMonth(Integer year, Integer month);
}
