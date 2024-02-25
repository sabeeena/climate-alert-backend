package kz.geowarning.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

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
    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station stationId;
    @ManyToOne
    @JoinColumn(name = "weather_id")
    private WeatherData weatherId;
    @Column(name = "danger_level", nullable = false)
    private String dangerLevel;
    private Date time;

}
