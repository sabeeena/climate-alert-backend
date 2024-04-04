package kz.geowarning.data.controller;

import kz.geowarning.data.entity.ForecastFireData;
import kz.geowarning.data.entity.dto.ForecastDTO;
import kz.geowarning.data.service.MLDataService;
import kz.geowarning.data.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
public class MLDataController {

    @Autowired
    private MLDataService mlDataService;

    @PostMapping(RestConstants.REST_FORECAST_DATA + "/station/{stationId}")
    public ForecastFireData getForecastByStationId(@PathVariable("stationId") String stationId) throws Exception {
        return mlDataService.getForecastByStation(stationId);
    }

    @PostMapping(RestConstants.REST_FORECAST_DATA + "/station/save/{stationId}")
    public ForecastFireData saveForecastByStationId(@PathVariable("stationId") String stationId) throws Exception {
        return mlDataService.saveForecastByStation(stationId);
    }

    @PostMapping(RestConstants.REST_FORECAST_DATA + "/getByFilter")
    public List<ForecastFireData> getFiresByFilter(@RequestBody ForecastDTO fireDataDTO) throws ParseException {
        return mlDataService.getByFilter(fireDataDTO);
    }
}
