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

    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    private String originalFilename;
}
