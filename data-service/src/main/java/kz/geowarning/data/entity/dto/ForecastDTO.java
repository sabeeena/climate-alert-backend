package kz.geowarning.data.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastDTO {
    private String regionId;
    private String latitude;
    private String longitude;
    private Timestamp dateFrom;
    private Timestamp dateTo;
    private String dangerLevelFrom;
    private String dangerLevelTo;
}
