package kz.geowarning.data.config;

import com.opencsv.exceptions.CsvException;
import kz.geowarning.data.entity.Station;
import kz.geowarning.data.repository.StationsRepository;
import kz.geowarning.data.service.EgovFireReportService;
import kz.geowarning.data.service.FireRTDataService;
import kz.geowarning.data.service.MLDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class Scheduler {

    private final FireRTDataService fireRTDataService;
    private final EgovFireReportService egovFireReportService;
    private final MLDataService mlDataService;
    private final StationsRepository stationsRepository;

    @Scheduled(cron = "0 0 */1 * * *")
    public void getActualFireData() throws IOException, CsvException {
        fireRTDataService.getDataAndSave();
    }

    @Scheduled(cron="0 00 00 20 11 ?")
    public void getActualReportData() throws IOException {
        egovFireReportService.getDataAndSave();
    }

    @Scheduled(cron="0 0 */1 * * *") // every hour
    public void updateFireForecasts() throws Exception {
        for (Station station : stationsRepository.getAllStations()) {
            mlDataService.saveForecastByStation(station.getId());
        }
    }
}
