package kz.geowarning.data.controller;

import kz.geowarning.data.entity.EarthquakeData;
import kz.geowarning.data.entity.dto.EarthquakeDTO;
import kz.geowarning.data.service.UsgsEarthquakeService;
import kz.geowarning.data.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EarthquakeDataController {

    @Autowired
    private UsgsEarthquakeService earthquakeService;

    @PostMapping(RestConstants.REST_EARTHQUAKE_DATA + "/getByFilter")
    public List<EarthquakeData> getFiresByFilter(@RequestBody EarthquakeDTO earthquakeDTO) {
        return earthquakeService.getByFilter(earthquakeDTO);
    }

}
