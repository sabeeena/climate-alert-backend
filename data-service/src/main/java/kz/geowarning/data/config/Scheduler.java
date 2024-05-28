package kz.geowarning.data.config;

import com.opencsv.exceptions.CsvException;
import kz.geowarning.data.entity.Station;
import kz.geowarning.data.repository.StationsRepository;
import kz.geowarning.data.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class Scheduler {

    private final FireRTDataService fireRTDataService;
    private final EgovFireReportService egovFireReportService;
    private final MLDataService mlDataService;
    private final StationsRepository stationsRepository;
    private final AlertService alertService;
    private final UsgsEarthquakeService earthquakeService;

    @Scheduled(cron = "0 0 */1 * * *")
    public void getActualFireData() throws IOException, CsvException {
        fireRTDataService.getDataAndSave();
    }

    @Scheduled(cron = "0 00 00 20 11 ?")
    public void getActualReportData() throws IOException {
        egovFireReportService.getDataAndSave();
    }

    @Scheduled(cron = "0 0 */1 * * *")// every hour
    public void updateFireForecasts() throws Exception {
        for (Station station : stationsRepository.getAllStations()) {
            mlDataService.saveForecastByStation(station.getId());
        }
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void sendRealtimeAlert() throws JSONException, IOException {
        alertService.alertRecipientsRealtime();
    }

    @Scheduled(cron = "0 0 */1 * * *")
    public void sendForecastAlert() throws JSONException, ParseException {
        alertService.alertRecipientsForecast();
    }

    @Scheduled(fixedRateString = "60000")
    public void updateEarthquakeData() throws IOException, CsvException, IllegalAccessException, JSONException {
        ZonedDateTime currentTime = ZonedDateTime.now();
        ZonedDateTime starttime = currentTime.minus(60, ChronoUnit.MINUTES);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        earthquakeService.getDataAndSave(starttime.format(formatter), null);
//        earthquakeService.getDataAndSave(null, currentTime.format(formatter));
    }
}
