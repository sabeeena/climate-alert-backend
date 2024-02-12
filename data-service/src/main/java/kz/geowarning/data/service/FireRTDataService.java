package kz.geowarning.data.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import kz.geowarning.data.entity.FireRTData;
import kz.geowarning.data.repository.FireRTDataRepository;
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
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class FireRTDataService {

    @Value("${api.nasa.url}")
    private String nasaApiUrl;

    @Value("${api.nasa.key}")
    private String nasaApiKey;

    private NasaService nasaService;

    @Autowired
    private FireRTDataRepository fireRTDataRepository;

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(nasaApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        nasaService = retrofit.create(NasaService.class);
    }

    public List<String[]> getRTData() throws IOException, CsvException {
        LocalDate currentDate = LocalDate.now().minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = currentDate.format(formatter);

        Call<ResponseBody> retrofitCall = nasaService.getRTData(nasaApiKey, "KAZ", "1", formattedDate);

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

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public FireRTData saveData(FireRTData data){
        return fireRTDataRepository.save(data);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public List<FireRTData> saveAllData(List<String[]> data){
        data.remove(0);
        List<FireRTData> dataList = new ArrayList<>();
        for (String[] row : data) {
            FireRTData fireData = new FireRTData();
            Field[] fields = fireData.getClass().getDeclaredFields();
            for (int i = 0; i < row.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);

                Object val = row[i];

                if (i == 6) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date utilDate = null;
                    try {
                        utilDate = dateFormat.parse(val.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                    field.set(fireData, sqlDate);
                } else if (i == 7) {
                    SimpleDateFormat timeFormat = new SimpleDateFormat("Hmm");
                    java.util.Date utilDate = null;
                    try {
                        utilDate = timeFormat.parse(val.toString());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    java.sql.Time sqlTime = new java.sql.Time(utilDate.getTime());
                    field.set(fireData, sqlTime);
                } else {
                    field.set(fireData, val.toString());
                }
            }
            dataList.add(fireData);
        }
        return fireRTDataRepository.saveAll(dataList);
    }

    public void getDataAndSave() throws IOException, CsvException {
        saveAllData(getRTData());
    }

    public List<FireRTData> getDataByDate(Date date) {
        return fireRTDataRepository.findAllByAcqDate(date);
    }

    public List<FireRTData> getDataByYearAndMonth(Integer year, Integer month, String email) {
        if(email == null) {
            System.out.println("email is not null");
            return fireRTDataRepository.findByYearAndMonthAndEmail(year, month, email);
        } else {System.out.println("email is null");
            return fireRTDataRepository.findByYearAndMonth(year, month);
        }
    }

    public FireRTData getById(Long id) {
        if(fireRTDataRepository.findById(id).isPresent()) {
            return fireRTDataRepository.findById(id).get();
        }
        return null;
    }
}
