package kz.geowarning.report.reportsevice.controller;

import kz.geowarning.report.reportsevice.dto.AssignmentDTO;
import kz.geowarning.report.reportsevice.entity.Assignment;
import kz.geowarning.report.reportsevice.entity.FireRealTimeReport;
import kz.geowarning.report.reportsevice.service.AssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping({"/assign"})
public class AssignController {
    @Autowired
    private AssignService assignService;
    @Value("${notification}")
    private String notification;

    @PutMapping("/fire-real-time")
    public ResponseEntity<Assignment> findAllCmRequestInfos(@RequestBody final AssignmentDTO assignment,
                                                            @RequestParam final Long id, HttpServletRequest httpServletRequest, HttpServletResponse response)  throws IOException {

        return ResponseEntity.ok().body(assignService.assign(assignment, id, FireRealTimeReport.class, httpServletRequest, response));
    }
}
