package kz.geowarning.report.reportsevice.dto;

import kz.geowarning.report.reportsevice.entity.Editor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FireReportCreateDto {
    Date startDate;
    double latitude;
    double longitude;
    private Set<Editor> editors;
    private Long fireRTDataId;
}
