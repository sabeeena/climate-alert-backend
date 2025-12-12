package kz.geowarning.data.repository;


import kz.geowarning.data.entity.Camera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CameraRepository extends JpaRepository<Camera, Long> {}