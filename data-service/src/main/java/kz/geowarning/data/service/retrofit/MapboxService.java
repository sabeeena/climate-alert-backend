package kz.geowarning.data.service.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MapboxService {

    @GET("geocoding/v5/mapbox.places/{longitude},{latitude}.json")
    Call<ResponseBody> reverseGeocode(
            @Path("longitude") String longitude,
            @Path("latitude") String latitude,
            @Query("access_token") String token,
            @Query("language") String language
    );
}
