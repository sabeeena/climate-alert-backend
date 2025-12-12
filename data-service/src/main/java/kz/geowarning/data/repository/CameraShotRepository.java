package kz.geowarning.data.repository;

import kz.geowarning.data.entity.CameraShot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraShotRepository extends JpaRepository<CameraShot, Long> {}
