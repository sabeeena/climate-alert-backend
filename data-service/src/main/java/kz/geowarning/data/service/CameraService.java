package kz.geowarning.data.service;

import kz.geowarning.data.entity.Camera;
import kz.geowarning.data.entity.dto.CameraCreateDTO;
import kz.geowarning.data.entity.dto.CameraStatus;
import kz.geowarning.data.repository.CameraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CameraService {

    private final CameraRepository cameraRepository;

    public Camera create(CameraCreateDTO dto) {
        Camera camera = new Camera();
        camera.setName(dto.getName());
        camera.setLatitude(dto.getLatitude());
        camera.setLongitude(dto.getLongitude());
        camera.setDescription(dto.getDescription());
        camera.setThreshold(dto.getThreshold());
        camera.setStatus(CameraStatus.ONLINE); // или NEW, как тебе удобно
        return cameraRepository.save(camera);
    }

    public List<Camera> findAll() {
        return cameraRepository.findAll();
    }

    public Camera getById(Long id) {
        return cameraRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Camera not found: " + id));
    }
}