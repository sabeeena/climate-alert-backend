package kz.geowarning.data.repository;

import kz.geowarning.data.entity.FireRTData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface FireRTDataRepository extends JpaRepository<FireRTData, Long> {

    List<FireRTData> findAllByAcqDate(Date date);
}
