package kz.geowarning.data.entity;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "camera_shot")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CameraShot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "camera_id")
    private Camera camera;

    private LocalDateTime timestamp;

    private String imageUrl; // MinIO URL

    private String originalFilename;
}
