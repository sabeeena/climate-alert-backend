package kz.geowarning.data.entity.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CameraEventDTO {

    private Long cameraId;

    private LocalDateTime timestamp;

    private Double confidence;

    private Boolean hasFire;

    /**
     * Картинка в Base64 (JPEG/PNG)
     */
    private String imageBase64;

    private String bboxJson;
}