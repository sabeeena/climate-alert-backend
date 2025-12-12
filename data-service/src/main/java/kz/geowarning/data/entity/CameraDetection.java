package kz.geowarning.data.entity;

import javax.persistence.*;

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

    /**
     * JSON-строка с bounding boxes от ML.
     * Например: [{"x1":10,"y1":20,"x2":100,"y2":150}, ...]
     */
    @Column(columnDefinition = "TEXT")
    private String bboxJson;

    private LocalDateTime createdAt = LocalDateTime.now();
}