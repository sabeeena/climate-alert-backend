package kz.geowarning.data.controller;

import kz.geowarning.data.entity.ForecastFireData;
import kz.geowarning.data.service.MLDataService;
import kz.geowarning.data.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MLDataController {

    @Autowired
    private MLDataService mlDataService;

    @PostMapping(RestConstants.REST_FORECAST_DATA + "/station/{stationId}")
    public ForecastFireData getForecastByStationId(@PathVariable("stationId") String stationId) throws IOException {
        return mlDataService.getForecastByStation(stationId);
    }

    @PostMapping(RestConstants.REST_FORECAST_DATA + "/station/save/{stationId}")
    public ForecastFireData saveForecastByStationId(@PathVariable("stationId") String stationId) throws IOException {
        return mlDataService.saveForecastByStation(stationId);
    }

}
