package kz.geowarning.data.service;

import kz.geowarning.data.entity.Region;
import kz.geowarning.data.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public Region saveRegion(Region region) {
        return regionRepository.save(region);
    }

}
