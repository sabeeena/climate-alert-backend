package kz.geowarning.data.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import kz.geowarning.data.entity.EarthquakeData;
import kz.geowarning.data.entity.Region;
import kz.geowarning.data.repository.EarthquakeDataRepository;
import kz.geowarning.data.repository.RegionRepository;
import kz.geowarning.data.service.retrofit.UsgsService;
import lombok.SneakyThrows;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class UsgsEarthquakeService {

    @Value("${api.usgs.url}")
    private String usgsUrlApi;

    @Autowired
    private EarthquakeDataRepository earthquakeDataRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private BingLocationsService bingLocationsService;

    private UsgsService usgsService;

    // It is not precise for now, will change to the valid one later
    private final String maxlatitude = "55.744785", minlatitude = "41.003595",
                         maxlongitude = "86.676809", minlongitude = "45.631890";

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(usgsUrlApi)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        usgsService = retrofit.create(UsgsService.class);
    }

    @Transactional(rollbackFor = Exception.class)
    public void getDataAndSave(String starttime, String endtime) throws IOException, CsvException, IllegalAccessException {
        List<EarthquakeData> data = covertDataToObject(getData(starttime, endtime));
        if (!data.isEmpty()) {
            saveData(data);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public List<String[]> getData(String starttime, String endtime) throws IOException, CsvException {

        Call<ResponseBody> retrofitCall = usgsService.getEarthquakeInfoByArea("csv", maxlatitude, minlatitude,
                                                                              maxlongitude, minlongitude, starttime, endtime);

        Response<ResponseBody> response = retrofitCall.execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : "Unknown error");
        }

        ResponseBody responseBody = response.body();
        String csvData = responseBody.string();
        StringReader stringReader = new StringReader(csvData);
        CSVReader csvReader = new CSVReaderBuilder(stringReader).build();

        return csvReader.readAll();
    }

    private List<EarthquakeData> covertDataToObject(List<String[]> data) throws IllegalAccessException, IOException {
        data.remove(0);
        List<EarthquakeData> dataList = new ArrayList<>();
        for (String[] row : data) {
            EarthquakeData earthquakeData = new EarthquakeData();
            Field[] fields = earthquakeData.getClass().getDeclaredFields();
            for (int i = 0; i < row.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                Object val = row[i];

                if (i == 0 || i == 12) {
                    ZonedDateTime time = ZonedDateTime.parse(val.toString(), DateTimeFormatter.ISO_DATE_TIME);
                    field.set(earthquakeData, time);
                } else {
                    field.set(earthquakeData, val.toString());
                }
            }
            List<Region> regions = regionRepository.findByNameEngCaseInsensitive(bingLocationsService.getAddressInfoByCoordinates(
                    earthquakeData.getLatitude(), earthquakeData.getLongitude()).getAdminDistrict());

            if (regions != null && !regions.isEmpty()) {
                earthquakeData.setRegionId(regions.get(0));
            } else {
                earthquakeData.setRegionId(regionRepository.findByNameEngCaseInsensitive("Undetected Region").get(0));
            }
            dataList.add(earthquakeData);
        }
        return dataList;
    }

    private void saveData(List<EarthquakeData> data) {
        earthquakeDataRepository.saveAll(data);
    }

}
