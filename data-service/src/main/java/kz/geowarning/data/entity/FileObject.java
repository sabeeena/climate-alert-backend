package kz.geowarning.data.entity;

import javax.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_object")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bucket;
    private String objectName;
    private Long size;
    private String contentType;

    private LocalDateTime createdAt = LocalDateTime.now();
}