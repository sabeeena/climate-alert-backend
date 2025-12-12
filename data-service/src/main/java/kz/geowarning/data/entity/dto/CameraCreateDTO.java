package kz.geowarning.data.entity.dto;

import lombok.Data;

@Data
public class CameraCreateDTO {
    private String name;
    private Double latitude;
    private Double longitude;
    private String description;
    private Double threshold;
}
