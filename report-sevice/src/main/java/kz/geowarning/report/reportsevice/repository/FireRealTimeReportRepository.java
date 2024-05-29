package kz.geowarning.report.reportsevice.repository;

import kz.geowarning.report.reportsevice.entity.FireRealTimeReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FireRealTimeReportRepository extends JpaRepository<FireRealTimeReport, Long> {
    List<FireRealTimeReport> findByFireRTDataId(Long fireRTDataId);

    @Query(nativeQuery = true, value = "SELECT public.fire_real_time_report.*\n" +
            "from data.firertdata, public.fire_real_time_report, public.editor\n" +
            "                     WHERE EXTRACT(YEAR FROM acq_date) =:year AND EXTRACT(month FROM acq_date) =:month\n" +
            "            and data.firertdata.id = public.fire_real_time_report.firertdata_id\n" +
            "                       and public.fire_real_time_report.firertdata_id = public.editor.report_id\n" +
            "                       and email =:email")
    List<FireRealTimeReport> findByYearAndMonth(Integer year, Integer month, String email);

    @Query(nativeQuery = true, value = "select * from fire_real_time_report where status_id=3")
    List<FireRealTimeReport> findByStatusApproved();
}
