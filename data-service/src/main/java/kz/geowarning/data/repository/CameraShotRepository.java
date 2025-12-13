package kz.geowarning.data.repository;

import kz.geowarning.data.entity.CameraShot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CameraShotRepository extends JpaRepository<CameraShot, Long> {
    List<CameraShot> findAllByCameraId(Long cameraId);
}
