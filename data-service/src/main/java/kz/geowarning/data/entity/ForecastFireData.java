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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "weatherId", referencedColumnName = "id")
    private WeatherData weather;
    private String dangerLevel;

}
