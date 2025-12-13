package kz.geowarning.data.entity;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.geowarning.data.entity.dto.DetectionStatus;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "cameradetection")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CameraDetection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "camera_shot_id")
    private CameraShot cameraShot;

    private Double confidence;
    private Boolean hasFire;

    @Enumerated(EnumType.STRING)
    private DetectionStatus status;

    @Column(columnDefinition = "TEXT")
    private String bboxJson;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

}