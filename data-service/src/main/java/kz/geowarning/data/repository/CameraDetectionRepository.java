package kz.geowarning.data.repository;

import kz.geowarning.data.entity.CameraDetection;
import kz.geowarning.data.entity.dto.DetectionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraDetectionRepository extends JpaRepository<CameraDetection, Long> {

    List<CameraDetection> findAllByOrderByCreatedAtDesc();

    List<CameraDetection> findByStatusOrderByCreatedAtDesc(
            DetectionStatus status
    );
}
