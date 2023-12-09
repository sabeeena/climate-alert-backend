package kz.geowarning.data.controller;

import kz.geowarning.data.entity.ReportYearlyData;
import kz.geowarning.data.service.EgovFireReportService;
import kz.geowarning.data.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class ReportDataController {

    @Autowired
    private EgovFireReportService egovFireReportService;

    @PostMapping(RestConstants.REST_REPORT_DATA + "/save")
    public void saveDataToDb() throws IOException {
        egovFireReportService.getDataAndSave();
    }

    @GetMapping(RestConstants.REST_REPORT_DATA + "/getData/{year}")
    public List<ReportYearlyData> getDataByYear(@PathVariable("year") String year) {
        return egovFireReportService.getDataByYear(year);
    }
}
