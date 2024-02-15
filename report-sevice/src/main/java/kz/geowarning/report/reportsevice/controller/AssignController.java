package kz.geowarning.report.reportsevice.controller;

import kz.geowarning.report.reportsevice.dto.AssignmentDTO;
import kz.geowarning.report.reportsevice.entity.Assignment;
import kz.geowarning.report.reportsevice.entity.FireRealTimeReport;
import kz.geowarning.report.reportsevice.service.AssignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Assignment> assignFireRT(@RequestBody final AssignmentDTO assignment,
                                                            @RequestParam final Long id, HttpServletRequest httpServletRequest, HttpServletResponse response)  throws IOException {

        return ResponseEntity.ok().body(assignService.assign(assignment, id, FireRealTimeReport.class, httpServletRequest, response));
    }
    @PutMapping("/fire-real-time/approve")
    public void approveFireRT(@RequestBody final Assignment assignment,
                                         HttpServletRequest httpServletRequest, HttpServletResponse response)  throws IOException {
     assignService.approval(assignment, httpServletRequest, response);
    }
}
