package kz.geowarning.data.controller;

import kz.geowarning.data.entity.EarthquakeData;
import kz.geowarning.data.entity.dto.EarthquakeDTO;
import kz.geowarning.data.service.UsgsEarthquakeService;
import kz.geowarning.data.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
public class EarthquakeDataController {

    @Autowired
    private UsgsEarthquakeService earthquakeService;

    @PostMapping(RestConstants.REST_EARTHQUAKE_DATA + "/save")
    public void getDataAndSave () throws Exception {
        ZonedDateTime currentTime = ZonedDateTime.now();
        ZonedDateTime starttime = currentTime.minus(30, ChronoUnit.DAYS);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        earthquakeService.getDataAndSave(starttime.format(formatter), null);
    }

    @PostMapping(RestConstants.REST_EARTHQUAKE_DATA + "/getByFilter")
    public List<EarthquakeData> getFiresByFilter(@RequestBody EarthquakeDTO earthquakeDTO) {
        return earthquakeService.getByFilter(earthquakeDTO);
    }

    @PostMapping(RestConstants.REST_EARTHQUAKE_DATA + "/download")
    public ResponseEntity<byte[]> downloadEarthquakesByFilter(@RequestBody EarthquakeDTO earthquakeDTO) {
        byte[] csvBytes = earthquakeService.convertToCsv(earthquakeDTO);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=usgs_earthquake_data.csv");
        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }
}
