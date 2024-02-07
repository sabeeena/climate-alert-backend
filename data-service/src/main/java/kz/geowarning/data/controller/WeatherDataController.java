package kz.geowarning.data.controller;

import kz.geowarning.data.entity.WeatherDTO;
import kz.geowarning.data.entity.WeatherData;
import kz.geowarning.data.service.WeatherDataService;
import kz.geowarning.data.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
public class WeatherDataController {

    @Autowired
    private WeatherDataService weatherDataService;

    @PostMapping(RestConstants.REST_WEATHER_DATA + "/hourly/station")
    public List<WeatherData> getWeatherHourlyDataByStationId(@RequestBody WeatherDTO weatherDTO) throws IOException {
        return weatherDataService.getHourlyDataByStationId(weatherDTO);
    }

}
