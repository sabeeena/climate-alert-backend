package kz.geowarning.report.reportsevice.service;

import kz.geowarning.report.reportsevice.dto.FireReportCreateDto;
import kz.geowarning.report.reportsevice.dto.FireReportF1Dto;
import kz.geowarning.report.reportsevice.entity.*;
import kz.geowarning.report.reportsevice.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class FireService {
    @Autowired
    private FireRealTimeEconomicDamageReportRepository fireRealTimeEconomicDamageReportRepository;
    @Autowired
    private FireRealTimeReportRepository fireRealTimeReportRepository;
    @Autowired
    private FireRealTimeVegetationInformationRepository fireRealTimeVegetationInformationRepository;
    @Autowired
    private EditorRepository editorRepository;

    @Autowired
    private StatusRepository statusRepository;

    public FireRealTimeReport createRealTimeNewReport(FireReportCreateDto dto){
        Status status = statusRepository.findById(1L).orElse(null);

        FireRealTimeEconomicDamageReport economicDamageReport = new FireRealTimeEconomicDamageReport();
        economicDamageReport.setStatus(status);
        FireRealTimeEconomicDamageReport savedEconomicDamageReport = fireRealTimeEconomicDamageReportRepository.save(economicDamageReport);

        FireRealTimeVegetationInformation vegetationInformation = new FireRealTimeVegetationInformation();
        vegetationInformation.setStatus(status);
        FireRealTimeVegetationInformation savedVegetationInformation = fireRealTimeVegetationInformationRepository.save(vegetationInformation);

        FireRealTimeReport fireRealTimeReport = new FireRealTimeReport();
        fireRealTimeReport.setStartDate(dto.getStartDate());
        fireRealTimeReport.setLatitude(dto.getLatitude());
        fireRealTimeReport.setLongitude(dto.getLongitude());
        fireRealTimeReport.setEconomicDamageReport(savedEconomicDamageReport);
        fireRealTimeReport.setVegetationInformation(savedVegetationInformation);
        fireRealTimeReport.setStatus(status);
        Set<Editor> editors = dto.getEditors();
        Set<Editor> savedEditors = new HashSet<>();

        for (Editor editor : editors) {
            Editor savedEditor = editorRepository.save(editor);
            savedEditors.add(savedEditor);
        }
        fireRealTimeReport.setEditors(savedEditors);
        fireRealTimeReport.setFireRTDataId(dto.getFireRTDataId());
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


    public List<FireRealTimeReport> getAllByRTDataId(Long rtDataId) {
        return fireRealTimeReportRepository.findByFireRTDataId(rtDataId);
    }
}
