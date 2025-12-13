package kz.geowarning.data.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraDetectionItemDto {

    private Long id;
    private Long cameraShotId;
    private String imageUrl;
    private Double confidence;
    private Boolean hasFire;
    private DetectionStatus status;
    private LocalDateTime createdAt;
}
