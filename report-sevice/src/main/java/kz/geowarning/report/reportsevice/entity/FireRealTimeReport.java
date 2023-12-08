package kz.geowarning.report.reportsevice.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fire_real_time_report")
public class FireRealTimeReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "region")
    private String region;

    @Column(name = "fire_area")
    private double fireArea;

    @Column(name = "fire_cause")
    private String fireCause;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @ManyToOne
    @JoinColumn(name = "fire_real_time_economic_damage_report_id")
    private FireRealTimeEconomicDamageReport economicDamageReport;

    @ManyToOne
    @JoinColumn(name = "fire_real_time_vegetation_information_id")
    private FireRealTimeVegetationInformation vegetationInformation;

    // Геттеры и сеттеры для полей

    // Дополнительные методы, конструкторы и т.д.
}
