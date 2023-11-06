package kz.geowarning.data.controller;

import com.opencsv.exceptions.CsvException;
import kz.geowarning.data.entity.FireRTData;
import kz.geowarning.data.service.FireRTDataService;
import kz.geowarning.data.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@RestController
public class FireRTDataController {

    @Autowired
    private FireRTDataService fireRTDataService;

    @GetMapping(RestConstants.REST_RT_DATA)
    public List<String[]> getRawRTData() throws IOException, CsvException {
        return fireRTDataService.getRTData();
    }

    @PostMapping(RestConstants.REST_RT_DATA + "/save")
    public void saveDataToDb() throws IOException, CsvException {
        fireRTDataService.getDataAndSave();
    }

    @GetMapping(RestConstants.REST_RT_DATA + "/date/{date}")
    public List<FireRTData> getDataByDate(@PathVariable("date") Date date) {
        return fireRTDataService.getDataByDate(date);
    }

}
