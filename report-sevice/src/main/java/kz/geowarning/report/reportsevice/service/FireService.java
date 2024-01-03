package kz.geowarning.report.reportsevice.service;

import kz.geowarning.report.reportsevice.dto.FireReportCreateDto;
import kz.geowarning.report.reportsevice.dto.FireReportF1Dto;
import kz.geowarning.report.reportsevice.entity.FireRealTimeEconomicDamageReport;
import kz.geowarning.report.reportsevice.entity.FireRealTimeReport;
import kz.geowarning.report.reportsevice.entity.FireRealTimeVegetationInformation;
import kz.geowarning.report.reportsevice.repository.FireRealTimeEconomicDamageReportRepository;
import kz.geowarning.report.reportsevice.repository.FireRealTimeReportRepository;
import kz.geowarning.report.reportsevice.repository.FireRealTimeVegetationInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FireService {
    @Autowired
    private FireRealTimeEconomicDamageReportRepository fireRealTimeEconomicDamageReportRepository;
    @Autowired
    private FireRealTimeReportRepository fireRealTimeReportRepository;
    @Autowired
    private FireRealTimeVegetationInformationRepository fireRealTimeVegetationInformationRepository;

    public FireRealTimeReport createRealTimeNewReport(FireReportCreateDto dto){
        FireRealTimeEconomicDamageReport economicDamageReport = fireRealTimeEconomicDamageReportRepository.save(new FireRealTimeEconomicDamageReport());
        FireRealTimeVegetationInformation vegetationInformation = fireRealTimeVegetationInformationRepository.save(new FireRealTimeVegetationInformation());

        FireRealTimeReport fireRealTimeReport = new FireRealTimeReport();
        fireRealTimeReport.setStartDate(dto.getStartDate());
        fireRealTimeReport.setLatitude(dto.getLatitude());
        fireRealTimeReport.setLongitude(dto.getLongitude());
        fireRealTimeReport.setEconomicDamageReport(economicDamageReport);
        fireRealTimeReport.setVegetationInformation(vegetationInformation);
        fireRealTimeReportRepository.save(fireRealTimeReport);

        return fireRealTimeReport;
    }

    public FireRealTimeReport editRealTimeReportF1(FireReportF1Dto dto){
        FireRealTimeReport fireRealTimeReport=null;
        if (fireRealTimeReportRepository.findById(dto.getId()).isPresent()) {
           fireRealTimeReport = fireRealTimeReportRepository.findById(dto.getId()).get();
        }
        FireRealTimeVegetationInformation fireRealTimeVegetationInformation=null;

        if (fireRealTimeVegetationInformationRepository.findById(fireRealTimeReport.getVegetationInformation().getId()).isPresent()) {
            if (fireRealTimeReport != null){
                fireRealTimeVegetationInformation = fireRealTimeVegetationInformationRepository.findById(fireRealTimeReport.getVegetationInformation().getId()).get();
            }
        }

        fireRealTimeReport.setStartDate(dto.getStartDate());
        fireRealTimeReport.setEndDate(dto.getStartDate());
        fireRealTimeReport.setRegion(dto.getRegion());
        fireRealTimeReport.setFireArea(dto.getFireArea());
        fireRealTimeReport.setFireCause(dto.getFireCause());
        fireRealTimeReport.setLatitude(dto.getLatitude());
        fireRealTimeReport.setLongitude(dto.getLongitude());

        fireRealTimeReportRepository.save(fireRealTimeReport);

        fireRealTimeVegetationInformation.setInformation(dto.getInformation());
        fireRealTimeVegetationInformation.setVegetationArea(dto.getVegetationArea());
        fireRealTimeVegetationInformation.setVegetationDensity(dto.getVegetationDensity());
        fireRealTimeVegetationInformation.setVegetationMoisture(dto.getVegetationMoisture());
        fireRealTimeVegetationInformation.setVegetationType(dto.getVegetationType());

        fireRealTimeVegetationInformationRepository.save(fireRealTimeVegetationInformation);

        return fireRealTimeReport;

    }

    public FireReportF1Dto getFireRealTimeF1(Long id){
        FireReportF1Dto dto = new FireReportF1Dto();
        FireRealTimeReport fireRealTimeReport=null;
        if (id != null) {
            if (fireRealTimeReportRepository.findById(id).isPresent()) {
                fireRealTimeReport = fireRealTimeReportRepository.findById(id).get();
            }
        }
        FireRealTimeVegetationInformation fireRealTimeVegetationInformation=null;

        if (fireRealTimeVegetationInformationRepository.findById(fireRealTimeReport.getVegetationInformation().getId()).isPresent()) {
            if (fireRealTimeReport != null){
                fireRealTimeVegetationInformation = fireRealTimeVegetationInformationRepository.findById(fireRealTimeReport.getVegetationInformation().getId()).get();
            }
        }
        dto.setId(fireRealTimeReport.getId());
        dto.setStartDate(fireRealTimeReport.getStartDate());
        dto.setEndDate(fireRealTimeReport.getEndDate());
        dto.setRegion(fireRealTimeReport.getRegion());
        dto.setFireArea(fireRealTimeReport.getFireArea());
        dto.setFireCause(fireRealTimeReport.getFireCause());
        dto.setLatitude(fireRealTimeReport.getLatitude());
        dto.setLongitude(fireRealTimeReport.getLongitude());
        dto.setVegetationType(fireRealTimeVegetationInformation.getVegetationType());
        dto.setVegetationArea(fireRealTimeVegetationInformation.getVegetationArea());
        dto.setVegetationDensity(fireRealTimeVegetationInformation.getVegetationDensity());
        dto.setVegetationMoisture(fireRealTimeVegetationInformation.getVegetationMoisture());
        dto.setInformation(fireRealTimeVegetationInformation.getInformation());
        return dto;
    }



}
