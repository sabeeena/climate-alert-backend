package kz.geowarning.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "forecastfiredata")
public class ForecastFireData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "forecastfiredata_seq")
    @SequenceGenerator(name = "forecastfiredata_seq", allocationSize = 1, sequenceName = "forecastfiredata_seq")
    private Long id;
    @Column(name = "station_id", nullable = false)
    private String stationId;
    @Column(name = "weather_id", nullable = false)
    private Long weatherId;
    @Column(name = "danger_level", nullable = false)
    private String dangerLevel;

}
