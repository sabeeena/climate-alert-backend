package kz.geowarning.report.reportsevice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FireReportF1Dto {

    private Long id;
    private Date startDate;
    private Date endDate;
    private String region;
    private double fireArea;
    private String fireCause;
    private double latitude;
    private double longitude;
    private String vegetationType;
    private double vegetationArea;
    private double vegetationDensity;
    private double vegetationMoisture;
    private String information;
}
