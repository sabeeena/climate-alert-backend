package kz.geowarning.data.service;

import com.google.gson.Gson;
import kz.geowarning.data.entity.Region;
import kz.geowarning.data.entity.dto.MapboxResponse;
import kz.geowarning.data.repository.RegionRepository;
import kz.geowarning.data.service.retrofit.MapboxService;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RegionDetectionService {

    @Autowired
    private RegionRepository regionRepository;

    private String mapboxToken = "pk.eyJ1IjoiZGVhZHBlYXJsIiwiYSI6ImNscGlibTE5eDBhZTgycXQ3c2Voa3lubjIifQ.P2SFPkK1FaRDrzfwcDGNAA";

    private MapboxService mapboxService;

    @PostConstruct
    public void init() {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(8, TimeUnit.SECONDS)
                .readTimeout(8, TimeUnit.SECONDS)
                .writeTimeout(8, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.mapbox.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mapboxService = retrofit.create(MapboxService.class);
    }

    private static final Map<String, String> REGION_MAP = Map.ofEntries(
            Map.entry("Almaty", "ALA"),
            Map.entry("Astana", "AST"),
            Map.entry("Shymkent", "SHY"),
            Map.entry("Almaty Region", "ALM"),
            Map.entry("Akmola Region", "AKM"),
            Map.entry("Aktobe Region", "AKT"),
            Map.entry("Atyrau Region", "ATY"),
            Map.entry("East Kazakhstan Region", "VOS"),
            Map.entry("Jambyl Region", "ZHA"),
            Map.entry("Jetisu Region", "ZHE"),
            Map.entry("Karaganda Region", "KAR"),
            Map.entry("Kostanay Region", "KUS"),
            Map.entry("Kyzylorda Region", "KZY"),
            Map.entry("Mangystau Region", "MAN"),
            Map.entry("North Kazakhstan Region", "SEV"),
            Map.entry("Pavlodar Region", "PAV"),
            Map.entry("Turkistan Region", "TUR"),
            Map.entry("Ulytau Region", "YUZ"),
            Map.entry("West Kazakhstan Region", "ZAP"),
            Map.entry("Abay Region", "ABA")
    );

    public Region detectRegion(String latitude, String longitude) throws IOException {

        Call<ResponseBody> call = mapboxService.reverseGeocode(
                longitude,
                latitude,
                mapboxToken,
                "en" // Можно "ru" или "kk"
        );

        Response<ResponseBody> response = call.execute();

        if (!response.isSuccessful()) {
            return regionRepository.findById("UNK").orElse(null);
        }

        String json = response.body().string();
        MapboxResponse mapbox = new Gson().fromJson(json, MapboxResponse.class);

        String regionName = extractRegionName(mapbox);

        String regionId = REGION_MAP.getOrDefault(regionName, "UNK");

        return regionRepository.findById(regionId)
                .orElse(regionRepository.findById("UNK").orElse(null));
    }


    private String extractRegionName(MapboxResponse mapbox) {

        if (mapbox == null || mapbox.getFeatures() == null || mapbox.getFeatures().isEmpty())
            return "Undetected Region";

        var feature = mapbox.getFeatures().get(0);

        if (feature.getContext() != null) {
            for (var ctx : feature.getContext()) {

                if (ctx.getId().startsWith("region")) {
                    return ctx.getText() + " Region";
                }

                if (ctx.getId().startsWith("place")) {
                    return ctx.getText();
                }
            }
        }

        return "Undetected Region";
    }
}
