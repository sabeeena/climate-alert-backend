package kz.geowarning.data.service;

import kz.geowarning.data.entity.FireRTData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface NasaService {
    @GET("country/csv/{apiKey}/VIIRS_SNPP_NRT/{countryCode}/{range}/{date}")
    Call<ResponseBody> getRTData(@Path("apiKey") String apiKey, @Path("countryCode") String countryCode,
                                 @Path("range") String range, @Path("date") String date);
}
