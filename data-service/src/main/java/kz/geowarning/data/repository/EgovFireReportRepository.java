package kz.geowarning.data.repository;

import kz.geowarning.data.entity.ReportYearlyData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EgovFireReportRepository extends JpaRepository<ReportYearlyData, Long> {

}
