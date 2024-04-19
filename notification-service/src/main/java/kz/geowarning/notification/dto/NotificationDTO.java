package kz.geowarning.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String receiverEmail;
    private String senderEmail;
    private String text;
    private String type;
    private String region;
    private String dangerPossibility;
    private String typeStatus;
    private String reportType;
    private Long reportId;
    private boolean seen;
    private String notificationType;
    private LocalDateTime sentTime;
}
