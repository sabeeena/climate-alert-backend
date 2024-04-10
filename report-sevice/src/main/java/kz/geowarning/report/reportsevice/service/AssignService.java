package kz.geowarning.report.reportsevice.service;

import kz.geowarning.report.reportsevice.dto.ApprovalRequest;
import kz.geowarning.report.reportsevice.dto.AssignmentDTO;
import kz.geowarning.report.reportsevice.dto.ReportNotificationDTO;
import kz.geowarning.report.reportsevice.entity.Agreement;
import kz.geowarning.report.reportsevice.entity.Assignment;
import kz.geowarning.report.reportsevice.entity.FireRealTimeReport;
import kz.geowarning.report.reportsevice.entity.Status;
import kz.geowarning.report.reportsevice.repository.AssignmentRepository;
import kz.geowarning.report.reportsevice.repository.FireRealTimeReportRepository;
import kz.geowarning.report.reportsevice.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Service
public class AssignService {
    @Autowired
    private FireRealTimeReportRepository fireRealTimeReportRepository;
    
    @Autowired
    private AssignmentRepository assignmentRepository;

    @Value("${notification}")
    private String notification;

    private final RestTemplate restTemplate;

    public AssignService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private StatusRepository statusRepository;
    public void sendNotification(ReportNotificationDTO reportNotificationDTO, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        String url = "/api/notification/service/notify-report";

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ReportNotificationDTO> httpEntity = new HttpEntity<>(reportNotificationDTO, httpHeaders);

        ResponseEntity<String> responseFromNSI = restTemplate.postForEntity(notification + url, httpEntity, String.class);

        response.setStatus(200);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        OutputStream outputStream = response.getOutputStream();

        if (responseFromNSI.getBody() != null) {
            outputStream.write(responseFromNSI.getBody().getBytes("UTF-8"));
        } else {
            // Handle the case where the response body is null
            // You may want to log a message, throw an exception, or send a default response
            outputStream.write("Default response".getBytes("UTF-8"));
        }

        outputStream.flush();
    }





    public Assignment assign(final AssignmentDTO assignment, final Long id,
                                final Class<? extends Agreement<?>> clazz, HttpServletRequest httpServletRequest, HttpServletResponse response) {
        AssignmentDTO assignmentDTOs = new AssignmentDTO();
        AssignmentDTO finalAssignmentDTOs = assignmentDTOs;
        Function<Agreement<?>, AssignmentDTO> function = (ass) -> {

            if (check(clazz, id)) {

                assignment.setEntityType(clazz.getSimpleName());
                assignment.setSentDateTime(LocalDateTime.now());

                try {
                    createAssignment(assignment, httpServletRequest, response);
                    return finalAssignmentDTOs;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {

                return null;
            }
        };

        if (clazz.equals(FireRealTimeReport.class) && check(FireRealTimeReport.class, id)) {
            FireRealTimeReport fireRealTimeReport = fireRealTimeReportRepository.findById(id).orElseThrow(null);
            if (Objects.nonNull(fireRealTimeReport)) {
                assignmentDTOs = function.apply(fireRealTimeReport);
                fireRealTimeReport.setAgreementId(assignmentDTOs.getId());
                Status status = statusRepository.findById(2L).orElse(null);
                fireRealTimeReport.setStatus(status);
                FireRealTimeReport save = fireRealTimeReportRepository.save(fireRealTimeReport);
            }
        }
        return assignmentDTOs.asAssigment();
    }

    @Transactional
    public void createAssignment(final AssignmentDTO assignmentDTO, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        assignmentDTO.setSentDateTime(LocalDateTime.now());
        Assignment assignment = assignmentDTO.asAssigment();
        assignment = assignmentRepository.save(assignment);
        ReportNotificationDTO reportNotificationDTO = new ReportNotificationDTO();
        reportNotificationDTO.setReceiverEmail(assignmentDTO.getUserIncoming());
        reportNotificationDTO.setSenderEmail(assignmentDTO.getUserOutComing());
        reportNotificationDTO.setTypeStatus(assignmentDTO.getComment());
        reportNotificationDTO.setReportType(assignmentDTO.getEntityType());
        reportNotificationDTO.setReportId(Long.parseLong(assignmentDTO.getEntityId()));
        reportNotificationDTO.setSenderAdmin(false);
        sendNotification(reportNotificationDTO, httpServletRequest, response);
    }


    private boolean check(final Class<? extends Agreement<?>> clazz, final Long id) {

        if (clazz.equals(FireRealTimeReport.class)) {
            return fireRealTimeReportRepository.existsById(id);
        }
        else {
            return false;
        }
    }

    public void approval(ApprovalRequest approvalRequest, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        Assignment assign = null;
        Assignment assignment = approvalRequest.getAssignment();
        String comment = approvalRequest.getComment();
        if (assignmentRepository.findById(assignment.getId()).isPresent()) {
            assign = assignmentRepository.findById(assignment.getId()).get();
        }

        if (check(FireRealTimeReport.class, Long.parseLong(assign.getEntityId())) &&
                FireRealTimeReport.class.toString().substring(FireRealTimeReport.class.toString().lastIndexOf(".") + 1).equals(assign.getEntityType())) {
            FireRealTimeReport fireRealTimeReport = fireRealTimeReportRepository.findById(Long.parseLong(assign.getEntityId())).orElseThrow(null);
            if (Objects.equals(assignment.getName(), "Approve")) {
                fireRealTimeReport.setApprovalComment(comment);
                Status status = statusRepository.findById(3L).orElse(null);
                fireRealTimeReport.setStatus(status);
            } else if (Objects.equals(assignment.getName(), "Correction")) {
                fireRealTimeReport.setApprovalComment(comment);
                Status status = statusRepository.findById(4L).orElse(null);
                fireRealTimeReport.setStatus(status);
            }
            ReportNotificationDTO reportNotificationDTO = new ReportNotificationDTO();
            reportNotificationDTO.setReceiverEmail(assign.getUserIncoming());
            reportNotificationDTO.setSenderEmail(assign.getUserOutComing());
            reportNotificationDTO.setTypeStatus(assignment.getName());
            reportNotificationDTO.setReportType(assign.getEntityType());
            reportNotificationDTO.setReportId(Long.parseLong(assign.getEntityId()));
            reportNotificationDTO.setSenderAdmin(true);
            fireRealTimeReportRepository.save(fireRealTimeReport);
            sendNotification(reportNotificationDTO, httpServletRequest, response);
        }
    }

    public List<Assignment> getAssignments(String email) {
        return assignmentRepository.findByUserIncoming(email);
    }
}
