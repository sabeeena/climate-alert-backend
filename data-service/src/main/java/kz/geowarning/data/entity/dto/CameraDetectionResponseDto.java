package kz.geowarning.data.entity.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import kz.geowarning.data.entity.dto.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CameraDetectionResponseDto {

    private Long cameraId;
    private Double latitude;
    private Double longitude;
    private String cameraName;
    private List<CameraDetectionItemDto> detections;
}
