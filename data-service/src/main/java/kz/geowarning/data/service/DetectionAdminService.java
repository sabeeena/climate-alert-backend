package kz.geowarning.data.service;

import kz.geowarning.data.entity.*;
import kz.geowarning.data.entity.dto.DetectionStatus;
import kz.geowarning.data.repository.CameraDetectionRepository;
import kz.geowarning.data.repository.FireRTDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

@Service
@RequiredArgsConstructor
public class DetectionAdminService {

    private final CameraDetectionRepository detectionRepository;
    private final FireRTDataRepository firertDataRepository;
    @Autowired
    private RegionDetectionService regionDetectionService;

    @Transactional
    public CameraDetection confirmDetection(Long detectionId) throws IOException {
        CameraDetection detection = detectionRepository.findById(detectionId)
                .orElseThrow(() -> new IllegalArgumentException("Detection not found: " + detectionId));

        detection.setStatus(DetectionStatus.CONFIRMED);

        // Создаём запись в firertdata
        CameraShot shot = detection.getCameraShot();
        Camera camera = shot.getCamera();
        Region region = regionDetectionService.detectRegion(camera.getLatitude().toString(), camera.getLongitude().toString());

        FireRTData data = FireRTData.builder()
                .country_id("KAZ")
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
                .regionId(region)
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
