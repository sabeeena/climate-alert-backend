package kz.geowarning.data.service;

import kz.geowarning.data.entity.Camera;
import kz.geowarning.data.entity.CameraDetection;
import kz.geowarning.data.entity.CameraShot;
import kz.geowarning.data.entity.FireRTData;
import kz.geowarning.data.entity.dto.DetectionStatus;
import kz.geowarning.data.repository.CameraDetectionRepository;
import kz.geowarning.data.repository.FireRTDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Time;

@Service
@RequiredArgsConstructor
public class DetectionAdminService {

    private final CameraDetectionRepository detectionRepository;
    private final FireRTDataRepository firertDataRepository;

    @Transactional
    public CameraDetection confirmDetection(Long detectionId) {
        CameraDetection detection = detectionRepository.findById(detectionId)
                .orElseThrow(() -> new IllegalArgumentException("Detection not found: " + detectionId));

        detection.setStatus(DetectionStatus.CONFIRMED);

        // Создаём запись в firertdata
        CameraShot shot = detection.getCameraShot();
        Camera camera = shot.getCamera();

        FireRTData data = FireRTData.builder()
                .country_id(null)
                .latitude(camera.getLatitude() != null ? camera.getLatitude().toString() : null)
                .longitude(camera.getLongitude() != null ? camera.getLongitude().toString() : null)
                .acqDate(Date.valueOf(shot.getTimestamp().toLocalDate()))
                .acqTime(Time.valueOf(shot.getTimestamp().toLocalTime()))
                .confidence(detection.getConfidence() != null ? detection.getConfidence().toString() : null)
                .satellite("CAMERA")
                .instrument("EOS R10")
                .version("v1")
                .daynight(null)
                .source("CAMERA")
                .cameraDetectionId(detection.getId())
                .build();


        firertDataRepository.save(data);

        return detection;
    }

    @Transactional
    public CameraDetection rejectDetection(Long detectionId) {
        CameraDetection detection = detectionRepository.findById(detectionId)
                .orElseThrow(() -> new IllegalArgumentException("Detection not found: " + detectionId));

        detection.setStatus(DetectionStatus.FALSE_ALARM);
        return detection;
    }
}
