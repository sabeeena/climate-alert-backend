package kz.geowarning.notification.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "report_notification")
public class ReportNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String receiverEmail;
    private String senderEmail;
    @Column(length = 20000)
    private String text;
    private String typeStatus;
    private String reportType;
    private Long reportId;
    private boolean seen = false;
    private LocalDateTime sentTime;

}
