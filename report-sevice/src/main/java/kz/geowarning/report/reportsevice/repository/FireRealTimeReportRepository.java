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
}
