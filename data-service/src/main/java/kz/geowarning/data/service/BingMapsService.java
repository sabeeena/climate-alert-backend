package kz.geowarning.data.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BingMapsService {
    @GET("Locations/{latitude},{longitude}")
    Call<ResponseBody> getLocationInfo(@Path("latitude") String latitude,
                                       @Path("longitude") String longitude,
                                       @Query("o") String outputFormat,
                                       @Query("key") String apiKey);
}
