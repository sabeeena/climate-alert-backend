package kz.geowarning.data.controller;

import kz.geowarning.data.service.EgovFireReportService;
import kz.geowarning.data.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class ReportDataController {

    @Autowired
    private EgovFireReportService egovFireReportService;

    @PostMapping(RestConstants.REST_REPORT_DATA + "/save")
    public void saveDataToDb() throws IOException {
        egovFireReportService.getDataAndSave();
    }

}
