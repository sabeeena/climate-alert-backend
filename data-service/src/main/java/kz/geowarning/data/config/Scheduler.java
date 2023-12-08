package kz.geowarning.data.config;

import com.opencsv.exceptions.CsvException;
import kz.geowarning.data.service.EgovFireReportService;
import kz.geowarning.data.service.FireRTDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class Scheduler {

    private final FireRTDataService fireRTDataService;
    private final EgovFireReportService egovFireReportService;

    @Scheduled(cron = "0 00 03 * * *")
    public void getActualFireData() throws IOException, CsvException {
        fireRTDataService.getDataAndSave();
    }

    @Scheduled(cron="0 00 00 20 11 ?")
    public void getActualReportData() throws IOException {
        egovFireReportService.getDataAndSave();
    }
}
