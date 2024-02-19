package kz.geowarning.data.service;

import kz.geowarning.data.entity.ForecastFireData;
import kz.geowarning.data.entity.dto.WeatherDTO;
import kz.geowarning.data.entity.WeatherData;
import kz.geowarning.data.repository.ForecastFireRepository;
import kz.geowarning.data.repository.WeatherRepository;
import kz.geowarning.data.service.retrofit.MLService;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MLDataService {

    @Value("${api.ml.url}")
    private String mlUrl;

    private MLService mlService;

    @Autowired
    private WeatherDataService weatherDataService;

    @Autowired
    private ForecastFireRepository forecastFireRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mlUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mlService = retrofit.create(MLService.class);
    }

    public ForecastFireData getForecastByStation(String stationId) throws IOException {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH");

        List<WeatherData> weather = weatherDataService.getHourlyDataByStationId(new WeatherDTO(stationId, currentDate.format(formatter),
                                                                    currentDate.format(formatter), currentDate.format(hourFormatter)));

        Call<ResponseBody> retrofitCall = mlService.getForecastByWeather(weather.get(0).getDwpt(), weather.get(0).getPres(),
                            weather.get(0).getRhum(), weather.get(0).getTemp(), weather.get(0).getWdir(), weather.get(0).getWspd());

        Response<ResponseBody> response = retrofitCall.execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : "Unknown error");
        }

        ResponseBody responseBody = response.body();
        String dangerLevel = responseBody.string();
        WeatherData weatherData = weatherRepository.save(weather.get(0));

        return new ForecastFireData(null, stationId, weatherData.getId(), dangerLevel);
    }

    public ForecastFireData saveForecastByStation(String stationId) throws IOException {
        return forecastFireRepository.save(getForecastByStation(stationId));
    }

}
