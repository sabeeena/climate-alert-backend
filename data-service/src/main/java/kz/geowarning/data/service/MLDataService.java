package kz.geowarning.data.service;

import kz.geowarning.data.entity.ForecastFireData;
import kz.geowarning.data.entity.dto.ForecastDTO;
import kz.geowarning.data.entity.dto.WeatherDTO;
import kz.geowarning.data.entity.WeatherData;
import kz.geowarning.data.repository.ForecastFireRepository;
import kz.geowarning.data.repository.StationsRepository;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

    @Autowired
    private StationsRepository stationsRepository;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mlUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mlService = retrofit.create(MLService.class);
    }

    public ForecastFireData getForecastByStation(String stationId) throws Exception {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter hourFormatter = DateTimeFormatter.ofPattern("HH");

        List<WeatherData> weather = weatherDataService.getHourlyDataByStationId(new WeatherDTO(stationId, currentDate.format(formatter),
                                                                    currentDate.format(formatter), currentDate.format(hourFormatter)));

        if (weather.isEmpty()) {
            throw new Exception("Couldn't Retrieve Weather Data For The Station");
        }

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

        return new ForecastFireData(null, stationsRepository.getReferenceById(stationId), weatherData, dangerLevel, dateFormat.parse(dateFormat.format(new Date())));
    }

    public ForecastFireData saveForecastByStation(String stationId) throws Exception {
        return forecastFireRepository.save(getForecastByStation(stationId));
    }

    public List<ForecastFireData> getByFilter(ForecastDTO fireDataDTO) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date currentDateTime = new Date();
        Date dateTimeAMonthAgo = Date.from(LocalDateTime.now().minusMonths(1).atZone(ZoneId.systemDefault()).toInstant());

        String currentDateTimeFormatted = dateFormat.format(currentDateTime);
        String dateTimeAMonthAgoFormatted = dateFormat.format(dateTimeAMonthAgo);

        Date parsedCurrentDateTime = dateFormat.parse(currentDateTimeFormatted);
        Date parsedDateTimeAMonthAgo = dateFormat.parse(dateTimeAMonthAgoFormatted);

        java.sql.Timestamp sqlCurrentDateTime = new java.sql.Timestamp(parsedCurrentDateTime.getTime());
        java.sql.Timestamp sqlDateTimeAMonthAgo = new java.sql.Timestamp(parsedDateTimeAMonthAgo.getTime());

        fireDataDTO.setLatitude(fireDataDTO.getLatitude() == null ? "0" : fireDataDTO.getLatitude());
        fireDataDTO.setLongitude(fireDataDTO.getLongitude() == null ? "0" : fireDataDTO.getLongitude());
        fireDataDTO.setRegionId(fireDataDTO.getRegionId() == null ? "0" : fireDataDTO.getRegionId());
        fireDataDTO.setDateFrom(fireDataDTO.getDateFrom() == null ? sqlDateTimeAMonthAgo : fireDataDTO.getDateFrom());
        fireDataDTO.setDateTo(fireDataDTO.getDateTo() == null ? sqlCurrentDateTime : fireDataDTO.getDateTo());
        fireDataDTO.setDangerLevelFrom(fireDataDTO.getDangerLevelFrom() == null ? "0" : fireDataDTO.getDangerLevelFrom());
        fireDataDTO.setDangerLevelTo(fireDataDTO.getDangerLevelTo() == null ? "0" : fireDataDTO.getDangerLevelTo());

        return forecastFireRepository.findByFilter(fireDataDTO);
    }

}
