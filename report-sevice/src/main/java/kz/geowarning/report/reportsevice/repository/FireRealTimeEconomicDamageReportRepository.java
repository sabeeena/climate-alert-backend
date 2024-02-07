package kz.geowarning.report.reportsevice.repository;

import kz.geowarning.report.reportsevice.entity.FireRealTimeEconomicDamageReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FireRealTimeEconomicDamageReportRepository extends JpaRepository<FireRealTimeEconomicDamageReport, Long> {
}
