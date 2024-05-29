package kz.geowarning.report.reportsevice.controller;

import kz.geowarning.report.reportsevice.dto.FireReportCreateDto;
import kz.geowarning.report.reportsevice.dto.FireReportF1Dto;
import kz.geowarning.report.reportsevice.dto.FireReportF2DTO;
import kz.geowarning.report.reportsevice.entity.FireRealTimeEconomicDamageReport;
import kz.geowarning.report.reportsevice.entity.FireRealTimeReport;
import kz.geowarning.report.reportsevice.service.FireService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
public class FireRealTimeController {
    @Autowired
    FireService fireService;

    @GetMapping("/search")
    public List<FireRealTimeReport> searchDataByYearAndMonth(@RequestParam("year") Integer year, @RequestParam("month") Integer month, String email) {
        return fireService.getDataByYearAndMonth(year, month, email);
    }

    @GetMapping("/get-one/fire-real-time")
    public ResponseEntity<?> getById(@RequestParam Long reportId) {
       return ResponseEntity.ok(fireService.getFireRealTimeF1(reportId));
    }
    @GetMapping("/get-one/economic-damage")
    public ResponseEntity<?> getEconomicDamage(@RequestParam Long reportId) {
        return ResponseEntity.ok(fireService.getFireRealTimeF2(reportId));
    }

    @GetMapping("/get-all/fire-real-time")
    public ResponseEntity<?> getAll(@RequestParam Long reportId) {
        return ResponseEntity.ok(fireService.getFireRealTimeF1(reportId));
    }

    @GetMapping("/get-all/approved")
    public ResponseEntity<?> getAllApproved() {
        return ResponseEntity.ok(fireService.getAllApproved());
    }

    @GetMapping("/get-one/approved")
    public ResponseEntity<?> getOneApproved(@RequestParam Long reportId) {
        return ResponseEntity.ok(fireService.getOneApproved(reportId));
    }

    @DeleteMapping("/delete/fire-real-time")
    public void deleteReport(@RequestParam Long reportId) {
        fireService.deleteFireRealTimeReport(reportId);
    }

    @GetMapping("/get-all/fire-real-time/by-rt-data-id")
    public ResponseEntity<?> getAllRtData(@RequestParam Long rtDataId) {
        return ResponseEntity.ok(fireService.getAllByRTDataId(rtDataId));
    }

    @PutMapping("/edit-f1/fire-real-time")
    public ResponseEntity<FireRealTimeReport> updateF1realTime(@RequestBody FireReportF1Dto dto){
        return ResponseEntity.ok(fireService.editRealTimeReportF1(dto));
    }
    @PutMapping("/edit-f2/fire-real-time")
    public ResponseEntity<FireRealTimeEconomicDamageReport> updateF2realTime(@RequestBody FireReportF2DTO dto){
        return ResponseEntity.ok(fireService.editRealTimeReportF2(dto));
    }

    @PostMapping("/create/fire-real-time")
    public ResponseEntity<FireRealTimeReport> createReport(@RequestBody FireReportCreateDto dto){
        return ResponseEntity.ok(fireService.createRealTimeNewReport(dto));
    }

//    @PostMapping("/create/fire-real-time")
//    public ResponseEntity<FireRealTimeReport> createReport(@RequestBody FireReportCreateDto dto){
//        return ResponseEntity.ok(fireService.createRealTimeNewReport(dto));
//    }
}
