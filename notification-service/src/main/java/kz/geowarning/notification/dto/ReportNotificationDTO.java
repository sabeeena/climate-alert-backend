package kz.geowarning.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportNotificationDTO {
    private String receiverEmail;
    private String senderEmail;
    private String typeStatus;
    private String reportType;
    private Long reportId;
    private boolean isSenderAdmin;

}
