package kz.geowarning.report.reportsevice.controller;

import kz.geowarning.report.reportsevice.dto.FireReportCreateDto;
import kz.geowarning.report.reportsevice.dto.FireReportF1Dto;
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

@RestController
public class FireRealTimeController {
    @Autowired
    FireService fireService;

    @GetMapping("/get-one/fire-real-time")
    public ResponseEntity<?> getById(@RequestParam Long reportId) {
       return ResponseEntity.ok(fireService.getFireRealTimeF1(reportId));
    }

    @GetMapping("/get-all/fire-real-time")
    public ResponseEntity<?> getAll(@RequestParam Long reportId) {
        return ResponseEntity.ok(fireService.getFireRealTimeF1(reportId));
    }

    @GetMapping("/get-all/fire-real-time/by-rt-data-id")
    public ResponseEntity<?> getAllRtData(@RequestParam Long rtDataId) {
        return ResponseEntity.ok(fireService.getAllByRTDataId(rtDataId));
    }

    @PutMapping("/edit-f1/fire-real-time")
    public ResponseEntity<FireRealTimeReport> updateF1realTime(@RequestBody FireReportF1Dto dto){
        return ResponseEntity.ok(fireService.editRealTimeReportF1(dto));
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
