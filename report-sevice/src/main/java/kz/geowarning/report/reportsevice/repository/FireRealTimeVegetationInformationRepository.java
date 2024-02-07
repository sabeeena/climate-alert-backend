package kz.geowarning.report.reportsevice.repository;

import kz.geowarning.report.reportsevice.entity.FireRealTimeVegetationInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FireRealTimeVegetationInformationRepository extends JpaRepository<FireRealTimeVegetationInformation, Long> {
}
