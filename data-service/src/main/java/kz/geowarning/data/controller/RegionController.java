package kz.geowarning.data.controller;

import kz.geowarning.data.entity.Region;
import kz.geowarning.data.service.RegionService;
import kz.geowarning.data.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegionController {

    @Autowired
    private RegionService regionService;

    @PostMapping(RestConstants.REST_REGIONS + "/save")
    public Region saveRegion(Region region) {
        return regionService.saveRegion(region);
    }

}
