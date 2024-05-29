package kz.geowarning.report.reportsevice.repository;

import kz.geowarning.report.reportsevice.entity.FireRealTimeEconomicDamageReport;
import kz.geowarning.report.reportsevice.entity.PublishReports;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublishReportsRepository extends JpaRepository<PublishReports, Long> {
    PublishReports findByReportId(Long reportId);
}
