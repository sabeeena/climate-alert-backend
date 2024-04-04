package kz.geowarning.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reportdata")
public class ReportYearlyData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reportdata_seq")
    @SequenceGenerator(name = "reportdata_seq", allocationSize = 1, sequenceName = "reportdata_seq")
    private Long id;

    private String year;
    private String region;
    private String deaths;
    @Column(name = "number_of_fires")
    private String numberOfFires;
    @Column(name = "amount_of_damage")
    private String amountOfDamage;
    private String injured;

}
