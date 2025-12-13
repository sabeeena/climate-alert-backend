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
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DetectionAdminService {

    private final CameraDetectionRepository detectionRepository;
    private final FireRTDataRepository firertDataRepository;
    @Autowired
    private RegionDetectionService regionDetectionService;
    private final AlertService alertService;
    private final BingLocationsService bingLocationsService;

    @Transactional
    public CameraDetection confirmDetection(Long detectionId) throws Exception {
        CameraDetection detection = detectionRepository.findById(detectionId)
                .orElseThrow(() -> new IllegalArgumentException("Detection not found: " + detectionId));

        detection.setStatus(DetectionStatus.CONFIRMED);

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
        notifySingleFireRTData(data);

        return detection;
    }

    private void notifySingleFireRTData(FireRTData fireRTData) throws Exception {

        if (fireRTData.getCameraDetectionId() == null) {
            return;
        }

        CameraDetection detection = detectionRepository
                .findById(fireRTData.getCameraDetectionId())
                .orElse(null);

        if (detection == null) {
            return;
        }

        CameraShot shot = detection.getCameraShot();
        String imageUrl = null;

        if (shot != null && shot.getImageUrl() != null && !shot.getImageUrl().isBlank()) {
            imageUrl = shot.getImageUrl();
        }

        List<Map<String, Object>> recipients =
                alertService.getRecipientsFromAuthService();

        if (recipients == null || recipients.isEmpty()) {
            return;
        }

        Region region = regionDetectionService.detectRegion(
                fireRTData.getLatitude(),
                fireRTData.getLongitude()
        );

        String regionText = resolveRegionDescription(region, "ENG");

        String fireDescription = String.format(
                "%s: %s.",
                fireRTData.getAcqTime(),
                regionText
        );
        System.out.println("CAMERA DETECTION: " + detection);
        System.out.println("CAMERASHOT: " + shot);
        System.out.println("CAMERASHOT IMAGE URL: " + shot.getImageUrl());

        List<String> fireOccurrences = List.of(fireDescription);

        for (Map<String, Object> user : recipients) {

            String email = (String) user.get("email");
            String firstName = (String) user.get("firstName");
            String lastName = (String) user.get("lastName");
            String language = (String) user.get("languageCode");

            Map<String, Object> locationMap =
                    (Map<String, Object>) user.get("location");

            String locationName = (String) locationMap.get("name");
            System.out.println("IMAGE URL: " + imageUrl);
            alertService.notifyWarningRealtimeImageUrl(
                    email,
                    firstName,
                    lastName,
                    locationName,
                    "1",
                    fireOccurrences,
                    imageUrl,
                    language
            );
        }
    }


    private String resolveRegionDescription(Region region, String language) {

        if (region == null || "UNK".equalsIgnoreCase(region.getId())) {
                    return "near your location";
        }

        switch (language) {
            case "KZ":
                return region.getName_kaz();
            case "RU":
                return region.getName_rus();
            default:
                return region.getName_eng();
        }
    }

    @Transactional
    public CameraDetection rejectDetection(Long detectionId) {
        CameraDetection detection = detectionRepository.findById(detectionId)
                .orElseThrow(() -> new IllegalArgumentException("Detection not found: " + detectionId));

        detection.setStatus(DetectionStatus.FALSE_ALARM);
        return detection;
    }
}
