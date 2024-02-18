package kz.geowarning.data.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FireDataDTO {
    private String regionId;
    private String latitude;
    private String longitude;
    private Date dateFrom;
    private Date dateTo;
}
