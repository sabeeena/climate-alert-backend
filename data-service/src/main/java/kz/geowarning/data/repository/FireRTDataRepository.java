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

    @Query(value="SELECT data.firertdata.* from data.firertdata, public.fire_real_time_report, public.editor\n" +
            "         WHERE EXTRACT(YEAR FROM acq_date) =:year AND EXTRACT(month FROM acq_date) =:month\n" +
            "and data.firertdata.id = public.fire_real_time_report.firertdata_id\n" +
            "           and public.fire_real_time_report.id = public.editor.report_id\n" +
            "           and email =:email", nativeQuery = true)
    List<FireRTData> findByYearAndMonth(Integer year, Integer month, String email);
}
