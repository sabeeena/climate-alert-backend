package kz.geowarning.report.reportsevice.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fire_real_time_vegetation_information")
public class FireRealTimeVegetationInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "vegetation_type")
    private String vegetationType;

    @Column(name = "vegetation_area")
    private double vegetationArea;

    @Column(name = "vegetation_density")
    private double vegetationDensity;

    @Column(name = "vegetation_moisture")
    private double vegetationMoisture;

    @Column(name = "information")
    private String information;
    @ManyToOne
    private Status status;
    private String authorEmail;

}

