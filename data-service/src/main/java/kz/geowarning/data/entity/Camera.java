package kz.geowarning.data.entity;

import javax.persistence.*;

import kz.geowarning.data.entity.dto.CameraStatus;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "camera")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Camera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double latitude;
    private Double longitude;

    private String description;

    @Enumerated(EnumType.STRING)
    private CameraStatus status;

    private Double threshold; // ML confidence threshold

    private LocalDateTime createdAt = LocalDateTime.now();
}
