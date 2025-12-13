package kz.geowarning.data.controller;

import kz.geowarning.data.entity.Camera;
import kz.geowarning.data.entity.CameraDetection;
import kz.geowarning.data.entity.CameraShot;
import kz.geowarning.data.entity.dto.CameraCreateDTO;
import kz.geowarning.data.entity.dto.CameraDetectionResponseDto;
import kz.geowarning.data.entity.dto.CameraEventDTO;
import kz.geowarning.data.entity.dto.DetectionStatus;
import kz.geowarning.data.repository.CameraDetectionRepository;
import kz.geowarning.data.service.CameraEventService;
import kz.geowarning.data.service.CameraDetectionService;
import kz.geowarning.data.service.CameraService;
import kz.geowarning.data.service.DetectionAdminService;
import kz.geowarning.data.service.MinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/data/cameras")
@RequiredArgsConstructor
public class CameraController {

    private final CameraEventService cameraEventService;
    private final DetectionAdminService detectionAdminService;
    private final MinioStorageService minioStorageService;
    private final CameraService cameraService;
    private final CameraDetectionService cameraDetectionService;


    @GetMapping("/ping")
    public String ping() {
        return "Camera API is up";
    }

    @PostMapping("/events")
    public CameraDetection receiveCameraEvent(@RequestBody CameraEventDTO request) {
        return cameraEventService.handleCameraEvent(request);
    }

    @PostMapping("/detections/{id}/confirm")
    public CameraDetection confirmDetection(@PathVariable Long id) throws Exception {
        return detectionAdminService.confirmDetection(id);
    }


    @PostMapping("/detections/{id}/reject")
    public CameraDetection rejectDetection(@PathVariable Long id) {
        return detectionAdminService.rejectDetection(id);
    }

    @PostMapping("/{cameraId}/upload")
    public Map<String, String> uploadToCamera(
            @PathVariable Long cameraId,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String imageUrl = minioStorageService.uploadImage(
                    file.getBytes(),
                    file.getContentType(),
                    cameraId
            );

            return Map.of(
                    "cameraId", cameraId.toString(),
                    "url", imageUrl
            );

        } catch (Exception e) {
            throw new RuntimeException("Upload failed", e);
        }
    }

    @GetMapping
    public List<Camera> getAllCameras() {
        return cameraService.findAll();
    }

    @PostMapping
    public Camera createCamera(@RequestBody CameraCreateDTO dto) {
        return cameraService.create(dto);
    }

    @GetMapping("/camerashot/{id}")
    public List<CameraShot> getCameraShots(@PathVariable Long id) {
        return cameraService.getCameraShots(id);
    }

    @GetMapping("/cameradetections")
    public List<CameraDetectionResponseDto> getDetections(
            @RequestParam(required = false) DetectionStatus status
    ) {
        return cameraDetectionService.getDetections(status);
    }

}
