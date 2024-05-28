package kz.geowarning.data.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.geowarning.data.entity.dto.WeatherDTO;
import kz.geowarning.data.entity.WeatherData;
import kz.geowarning.data.repository.WeatherRepository;
import kz.geowarning.data.service.retrofit.MeteostatService;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class WeatherDataService {

    @Value("${api.meteostat.url}")
    private String meteostatApiUrl;

    @Value("${api.meteostat.key}")
    private String meteostatApiKey;

    private MeteostatService meteostatService;

    @Autowired
    private WeatherRepository weatherRepository;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Request request = chain.request().newBuilder()
                        .addHeader("X-RapidAPI-Key", meteostatApiKey)
                        .addHeader("X-RapidAPI-Host", "meteostat.p.rapidapi.com")
                        .build();
                return chain.proceed(request);
            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(meteostatApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        meteostatService = retrofit.create(MeteostatService.class);
    }

    public List<WeatherData> getHourlyDataByStationId(WeatherDTO weatherDTO) throws IOException {
        Call<ResponseBody> retrofitCall = meteostatService.getHourlyDataByStationId(
                weatherDTO.getStationId(),
                weatherDTO.getStartDate(),
                weatherDTO.getEndDate()
        );

        Response<ResponseBody> response = retrofitCall.execute();

        if (!response.isSuccessful()) {
            System.out.println("Couldn't Retrieve Weather Data For The Station");
            return null;
        }

        ResponseBody responseBody = response.body();
        String jsonString = responseBody.string();

        List<WeatherData> convertedData = convertFromJson(jsonString, weatherDTO.getHour());
        return convertedData;
    }

    public List<WeatherData> saveWeatherData(WeatherDTO weatherDTO) throws IOException {
        return weatherRepository.saveAll(getHourlyDataByStationId(weatherDTO));
    }

    private static List<WeatherData> convertFromJson(String jsonString, String hour) {
        List<WeatherData> dataList = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode readJson = mapper.readTree(jsonString);
            JsonNode data = readJson.get("data");

            for (JsonNode jsonNode : data) {
                if (!hour.isEmpty()) {
                    Date time = dateFormat.parse(String.valueOf(jsonNode.get("time")).substring(1, String.valueOf(jsonNode.get("time")).length() - 1));

                    SimpleDateFormat hourFormatter = new SimpleDateFormat("HH");
                    if (!Objects.equals(String.valueOf(Integer.parseInt(hourFormatter.format(time))), hour)) {
                        continue;
                    }
                }
                WeatherData weather = new WeatherData();
                weather.setTime(convertDate(String.valueOf(jsonNode.get("time")).substring(1, String.valueOf(jsonNode.get("time")).length() - 1)));
                weather.setTemp(String.valueOf(jsonNode.get("temp")));
                weather.setDwpt(String.valueOf(jsonNode.get("dwpt")));
                weather.setWspd(String.valueOf(jsonNode.get("wspd")));
                weather.setWdir(String.valueOf(jsonNode.get("wdir")));
                weather.setRhum(String.valueOf(jsonNode.get("rhum")));
                weather.setPres(String.valueOf(jsonNode.get("pres")));
                dataList.add(weather);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }

    private static Date convertDate(String dateString) {
        try {
            Date date = dateFormat.parse(dateString);
            return date;
        } catch (ParseException e) {
            System.out.println("Error parsing date: " + e.getMessage());
            return null;
        }
    }

}
