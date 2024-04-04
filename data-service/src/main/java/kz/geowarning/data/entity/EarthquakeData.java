package kz.geowarning.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "earthquakedata")
public class EarthquakeData {
    // Format sample: 2024-03-23T03:45:31.363Z
    private ZonedDateTime time;
    private String latitude;
    private String longitude;
    private String depth;
    private String mag;
    @Column(name = "mag_type")
    private String magType;
    private String nst;
    private String gap;
    private String dmin;
    private String rms;
    private String net;
    @Column(name = "usgs_id")
    private String usgsId;
    private ZonedDateTime updated;
    private String place;
    private String type;
    @Column(name = "horizontal_error")
    private String horizontalError;
    @Column(name = "depth_error")
    private String depthError;
    @Column(name = "mag_error")
    private String magError;
    @Column(name = "mag_nst")
    private String magNst;
    private String status;
    @Column(name = "location_source")
    private String locationSource;
    @Column(name = "mag_source")
    private String magSource;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "earthquakedata_seq")
    @SequenceGenerator(name = "earthquakedata_seq", allocationSize = 1, sequenceName = "earthquakedata_seq")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "region_id")
    private Region regionId;
}
