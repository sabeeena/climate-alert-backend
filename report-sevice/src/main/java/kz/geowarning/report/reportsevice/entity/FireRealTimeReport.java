package kz.geowarning.report.reportsevice.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fire_real_time_report")
public class FireRealTimeReport implements Agreement<FireRealTimeReport>{

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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "report_editors",
            joinColumns = @JoinColumn(name = "report_id"),
            inverseJoinColumns = @JoinColumn(name = "editor_id"))
    private Set<Editor> editors = new HashSet<>();

    @ManyToOne
    private Status status;

    private Long fireRTDataId;
    private Long agreementId;

    @Override
    public FireRealTimeReport agreed() {
        this.status.setId(3L);
        this.status.setName("согласовано");
        return this;
    }

    @Override
    public Long getAgreementId() {
        return agreementId;
    }

    @Override
    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }
}
