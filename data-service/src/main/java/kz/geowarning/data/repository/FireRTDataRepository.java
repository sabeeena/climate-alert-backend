package kz.geowarning.data.repository;

import kz.geowarning.data.entity.FireRTData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FireRTDataRepository extends JpaRepository<FireRTData, Long> {

}
