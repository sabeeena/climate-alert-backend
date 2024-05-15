package kz.geowarning.notification.service;

import kz.geowarning.notification.service.retrofit.MobizonService;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class SMSService {
    @Value("${api.mobizon.url}")
    private String mobizonApiUrl;

    @Value("${api.mobizon.key}")
    private String mobizonApiKey;

    private MobizonService mobizonService;

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mobizonApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mobizonService = retrofit.create(MobizonService.class);
    }

    public ResponseBody sendSMSMessage(String recipient, String text) throws IOException {

        Call<ResponseBody> retrofitCall = mobizonService.sendIndividualSms(recipient, null, text, mobizonApiKey);

        Response<ResponseBody> response = retrofitCall.execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : "Cannot send the SMS, service is unavailable.");
        }

        return response.body();
    }

}
