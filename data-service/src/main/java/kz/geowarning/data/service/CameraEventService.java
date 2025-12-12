package kz.geowarning.data.service;


import kz.geowarning.data.entity.Camera;
import kz.geowarning.data.entity.CameraDetection;
import kz.geowarning.data.entity.CameraShot;
import kz.geowarning.data.entity.dto.CameraEventDTO;
import kz.geowarning.data.entity.dto.DetectionStatus;
import kz.geowarning.data.repository.CameraDetectionRepository;
import kz.geowarning.data.repository.CameraRepository;
import kz.geowarning.data.repository.CameraShotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class CameraEventService {

    private final CameraRepository cameraRepository;
    private final CameraShotRepository cameraShotRepository;
    private final CameraDetectionRepository detectionRepository;
    private final MinioStorageService minioStorageService;

    public CameraDetection handleCameraEvent(CameraEventDTO request) {
        // 1. Найти камеру
        Camera camera = cameraRepository.findById(request.getCameraId())
                .orElseThrow(() -> new IllegalArgumentException("Camera not found: " + request.getCameraId()));

        // 2. Декодировать картинку
        byte[] imageBytes = Base64.getDecoder().decode(request.getImageBase64());

        // 3. Загрузить в MinIO
        String imageUrl = minioStorageService.uploadImage(imageBytes, "image/jpeg", camera.getId());

        // 4. Создать CameraShot
        CameraShot shot = CameraShot.builder()
                .camera(camera)
                .timestamp(request.getTimestamp() != null ? request.getTimestamp() : LocalDateTime.now())
                .imageUrl(imageUrl)
                .originalFilename(null) // можно передавать из Python, если нужно
                .build();
        cameraShotRepository.save(shot);

        // 5. Создать Detection со статусом NEW
        CameraDetection detection = CameraDetection.builder()
                .cameraShot(shot)
                .confidence(request.getConfidence())
                .hasFire(request.getHasFire())
                .bboxJson(request.getBboxJson())
                .status(DetectionStatus.NEW)
                .build();

        return detectionRepository.save(detection);
    }
}
