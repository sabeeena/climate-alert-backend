package kz.geowarning.report.reportsevice.controller;

import kz.geowarning.report.reportsevice.dto.FireReportF1Dto;
import kz.geowarning.report.reportsevice.entity.FireRealTimeReport;
import kz.geowarning.report.reportsevice.service.FireService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
public class FireRealTimeController {
    @Autowired
    FireService fireService;

    @GetMapping("/getById/fire-real-time")
    public ResponseEntity<?> getById(@RequestParam Long reportId) {
       return ResponseEntity.ok(fireService.getFireRealTimeF1(reportId));
    }

    @PutMapping("/edit-f1/fire-real-time")
    public ResponseEntity<FireRealTimeReport> updateF1realTime(@RequestParam FireReportF1Dto dto){
        return ResponseEntity.ok(fireService.editRealTimeReportF1(dto));
    }
}
