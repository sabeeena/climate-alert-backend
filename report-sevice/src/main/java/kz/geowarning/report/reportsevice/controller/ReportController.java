package kz.geowarning.report.reportsevice.controller;

import kz.geowarning.report.reportsevice.service.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
public class ReportController {
    @Autowired
    ReportService reportService;

    @GetMapping("/fire-real-time-overall")
    public ResponseEntity<?> generateAccessDenials(@RequestParam Long reportId,
                                                   @RequestParam String lang, @RequestParam String type) throws JRException, SQLException, IOException, IllegalAccessException {
        byte[] bytes = reportService.exportReportFireRealTimeOverall(reportId, lang);
        ByteArrayResource resource = new ByteArrayResource(bytes);

        if (bytes.length == 0) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=Report." + type)
                    .body(resource);
        }
    }

}
//http://localhost:8086/report/fire-real-time-overall?reportId=1&lang=ru&type=pdf