package kz.geowarning.data.entity.dto;

import lombok.Data;
import java.util.List;

@Data
public class MapboxResponse {
    private List<Feature> features;

    @Data
    public static class Feature {
        private String place_name;
        private List<Context> context;
    }

    @Data
    public static class Context {
        private String id;
        private String text;
    }
}
