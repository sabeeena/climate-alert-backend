package kz.geowarning.data.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EarthquakeDTO {
    private String regionId;
    private String latitude;
    private String longitude;
    private String dateFrom;
    private String dateTo;
    private String magnitudeFrom;
    private String magnitudeTo;
    private String depthFrom;
    private String depthTo;
}
