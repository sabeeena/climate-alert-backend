package kz.geowarning.data.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.geowarning.data.entity.ReportYearlyData;
import kz.geowarning.data.repository.EgovFireReportRepository;
import kz.geowarning.data.service.retrofit.EgovService;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class EgovFireReportService {

    @Value("${api.egov.url}")
    private String egovUrl;

    @Value("${api.egov.key}")
    private String egovApiKey;

    private EgovService egovService;

    @Autowired
    private EgovFireReportRepository egovFireReportRepository;

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(egovUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        egovService = retrofit.create(EgovService.class);
    }

    public List<ReportYearlyData> getDataAndSave() throws IOException {

        Call<ResponseBody> retrofitCall = egovService.getData(egovApiKey);

        Response<ResponseBody> response = retrofitCall.execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : "Unknown error");
        }

        ResponseBody responseBody = response.body();
        String jsonString = responseBody.string();

        List<ReportYearlyData> convertedData = convertFromJson(jsonString);

        return egovFireReportRepository.saveAll(convertedData);
    }

    public List<ReportYearlyData> getDataByYear(String year) {
        return egovFireReportRepository.getAllByYear(year);
    }

    private static List<ReportYearlyData> convertFromJson(String jsonString) {
        List<ReportYearlyData> dataList = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonArray = mapper.readTree(jsonString);

            for (JsonNode jsonNode : jsonArray) {
                ReportYearlyData dto1 = new ReportYearlyData();
                ReportYearlyData dto2 = new ReportYearlyData();

                dto1.setYear("2022");

                dto1.setInjured(String.valueOf(jsonNode.get("travmirovano_ludei__-_2022_god")));
                dto1.setDeaths(String.valueOf(jsonNode.get("gibel_ludei_-_2022_god")));
                dto1.setRegion(jsonNode.get("oblasti").asText());
                dto1.setAmountOfDamage(String.valueOf(jsonNode.get("summa_usherba_-_2022_(tis.tenge)")));
                dto1.setNumberOfFires(String.valueOf(jsonNode.get("kolichestvo_pojarov_-_2022_god")));

                dto2.setYear("2023");

                dto2.setInjured(String.valueOf(jsonNode.get("travmirovano_ludei__-_2023_god")));
                dto2.setDeaths(String.valueOf(jsonNode.get("gibel_ludei_-_2023_god")));
                dto2.setRegion(jsonNode.get("oblasti").asText());
                dto2.setAmountOfDamage(String.valueOf(jsonNode.get("summa_usherba_-_2023_(tis.tenge)")));
                dto2.setNumberOfFires(String.valueOf(jsonNode.get("kolichestvo_pojarov_-_2023_god")));

                dataList.add(dto1);
                dataList.add(dto2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataList;
    }

}
