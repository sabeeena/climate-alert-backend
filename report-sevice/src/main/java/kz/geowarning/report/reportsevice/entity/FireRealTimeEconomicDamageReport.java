package kz.geowarning.report.reportsevice.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "fire_real_time_economic_damage_report")
public class FireRealTimeEconomicDamageReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "agriculture_damage")
    private double agricultureDamage;

    @Column(name = "forestry_damage")
    private double forestryDamage;

    @Column(name = "infrastructure_damage")
    private double infrastructureDamage;

    @Column(name = "total_economic_damage")
    private double totalEconomicDamage;

    @Column(name = "firefighting_costs")
    private double firefightingCosts;

    @Column(name = "analysis", length = 10000)
    private String analysis;

    @Column(name = "conclusions", length = 10000)
    private String conclusions;

    @ManyToOne
    private Status status;
    private String authorEmail;

    private String harmfulSubstancesEmissions;
    private String impactOnLocalClimateAndEcosystem;
    private String damageToFloraAndFauna;
    private String lossesForLocalCommunitiesAndEconomy;
    private String aidToAffected;
    private String restorationAndRehabilitationCosts;
    private String elevationAboveSeaLevel;
    private String adjacentAffectedObjectsInfo;
    private String fireIncidenceTrends;
    private String comparisonWithPriorFires;
    private String evaluationOfFirefightingAndPreventionMeasures;
    // Геттеры и сеттеры для полей

    // Дополнительные методы, конструкторы и т.д.
}
