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
@Table(name = "weatherdata")
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "weatherdata_seq")
    @SequenceGenerator(name = "weatherdata_seq", allocationSize = 1, sequenceName = "weatherdata_seq")
    private Long id;
    private Date time;
    private String temp;
    /** Dew Point */
    private String dwpt;
    /** Relative Humidity */
    private String rhum;
    /** Wind (From) Direction */
    private String wdir;
    /** Average Wind Speed */
    private String wspd;
    /** Sea-Level Air Pressure */
    private String pres;
}
