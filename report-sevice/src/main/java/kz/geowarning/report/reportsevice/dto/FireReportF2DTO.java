package kz.geowarning.report.reportsevice.dto;

import kz.geowarning.report.reportsevice.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FireReportF2DTO {
    private Long id;
    private double agricultureDamage;
    private double forestryDamage;
    private double infrastructureDamage;
    private double totalEconomicDamage;
    private double firefightingCosts;
    private String analysis;
    private String conclusions;
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
}
