package kz.geowarning.data.service.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsgsService {

    @GET("query")
    Call<ResponseBody> getEarthquakeInfoByArea(@Query("format") String format,
                                               @Query("maxlatitude") String maxlatitude,
                                               @Query("minlatitude") String minlatitude,
                                               @Query("maxlongitude") String maxlongitude,
                                               @Query("minlongitude") String minlongitude,
                                               @Query("starttime") String starttime,
                                               @Query("endtime") String endtime);

}
