package kz.geowarning.data.service.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MeteostatService {
    @GET("stations/hourly")
    Call<ResponseBody> getHourlyDataByStationId(
            @Query("station") String stationId,
            @Query("start") String startDate,
            @Query("end") String endDate
    );
}
