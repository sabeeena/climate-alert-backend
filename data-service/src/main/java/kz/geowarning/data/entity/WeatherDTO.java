package kz.geowarning.data.entity;

import lombok.Data;

@Data
public class WeatherDTO {
    private String stationId;
    private String startDate; // YYYY-MM-DD
    private String endDate; // YYYY-MM-DD
    private String hour;
}
