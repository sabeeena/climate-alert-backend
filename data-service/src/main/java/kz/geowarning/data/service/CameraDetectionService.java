package kz.geowarning.data.service;

import kz.geowarning.data.entity.Camera;
import kz.geowarning.data.entity.CameraDetection;
import kz.geowarning.data.entity.CameraShot;
import kz.geowarning.data.entity.dto.CameraDetectionItemDto;
import kz.geowarning.data.entity.dto.CameraDetectionResponseDto;
import kz.geowarning.data.entity.dto.DetectionStatus;
import kz.geowarning.data.repository.CameraDetectionRepository;
import kz.geowarning.data.repository.CameraShotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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

    public List<CameraDetectionResponseDto> getDetections(DetectionStatus status) {

        List<CameraDetection> detections = (status == null)
                ? detectionRepository.findAllByOrderByCreatedAtDesc()
                : detectionRepository.findByStatusOrderByCreatedAtDesc(status);

        Map<Camera, List<CameraDetection>> grouped =
                detections.stream()
                        .collect(Collectors.groupingBy(
                                d -> d.getCameraShot().getCamera()
                        ));

        return grouped.entrySet().stream()
                .map(entry -> buildCameraDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private CameraDetectionResponseDto buildCameraDto(
            Camera camera,
            List<CameraDetection> detections
    ) {
        CameraDetectionResponseDto dto = new CameraDetectionResponseDto();
        dto.setCameraId(camera.getId());
        dto.setLatitude(camera.getLatitude());
        dto.setLongitude(camera.getLongitude());
        dto.setCameraName(camera.getName());

        List<CameraDetectionItemDto> items = detections.stream()
                .map(this::toDetectionItemDto)
                .collect(Collectors.toList());

        dto.setDetections(items);
        return dto;
    }

    private CameraDetectionItemDto toDetectionItemDto(CameraDetection d) {
        CameraDetectionItemDto dto = new CameraDetectionItemDto();
        dto.setId(d.getId());
        dto.setCameraShotId(d.getCameraShot().getId());
        dto.setImageUrl(d.getCameraShot().getImageUrl());
        dto.setConfidence(d.getConfidence());
        dto.setHasFire(d.getHasFire());
        dto.setStatus(d.getStatus());
        dto.setCreatedAt(d.getCreatedAt());
        return dto;
    }
}
