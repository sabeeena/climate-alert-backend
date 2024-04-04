package kz.geowarning.data.service.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EgovService {
    @GET("ortter_men_olardyn_saldarlaryn/v8")
    Call<ResponseBody> getData(@Query("apiKey") String apiKey);
}
