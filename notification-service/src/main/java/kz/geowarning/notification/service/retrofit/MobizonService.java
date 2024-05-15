package kz.geowarning.notification.service.retrofit;

import okhttp3.ResponseBody;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MobizonService {
    @GET("message/sendsmsmessage")
    Call<ResponseBody> sendIndividualSms(
            @Query("recipient") String recipient,
            @Query("from") String from,
            @Query("text") String text,
            @Query("apiKey") String apiKey
    );

}
