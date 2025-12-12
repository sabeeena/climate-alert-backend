package kz.geowarning.data.service;

import kz.geowarning.data.entity.CameraDetection;
import kz.geowarning.data.entity.CameraShot;
import kz.geowarning.data.entity.dto.DetectionStatus;
import kz.geowarning.data.repository.CameraDetectionRepository;
import kz.geowarning.data.repository.CameraShotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CameraDetectionService {

    private final CameraDetectionRepository detectionRepository;
    private final CameraShotRepository cameraShotRepository;

    public CameraDetection createDetection(CameraShot shot, double confidence, boolean hasFire) {
        CameraDetection detection = CameraDetection.builder()
                .cameraShot(shot)
                .confidence(confidence)
                .hasFire(hasFire)
                .status(DetectionStatus.NEW)
                .build();

        return detectionRepository.save(detection);
    }
}
