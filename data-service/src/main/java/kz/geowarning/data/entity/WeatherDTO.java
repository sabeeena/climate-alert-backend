package kz.geowarning.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDTO {
    private String stationId;
    private String startDate; // YYYY-MM-DD
    private String endDate; // YYYY-MM-DD
    private String hour;
}
