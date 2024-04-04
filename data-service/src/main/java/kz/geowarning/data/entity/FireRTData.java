package kz.geowarning.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "firertdata")
public class FireRTData {
    private String country_id;
    private String latitude;
    private String longitude;
    private String bright_ti4;
    private String scan;
    private String track;
    @Column(name = "acq_date", nullable = false)
    private Date acqDate;
    @Column(name = "acq_time", nullable = false)
    private Time acqTime;
    private String satellite;
    private String instrument;
    private String confidence;
    private String version;
    private String bright_ti5;
    private String frp;
    private String daynight;
    @Column(name = "region_id", nullable = false)
    private String regionId;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "firertdata_seq")
    @SequenceGenerator(name = "firertdata_seq", allocationSize = 1, sequenceName = "firertdata_seq")
    private Long id;
}
