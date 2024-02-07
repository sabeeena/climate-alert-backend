package kz.geowarning.data.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface MLService {
    @FormUrlEncoded
    @POST("predict_by_station_id")
    Call<ResponseBody> getForecastByWeather(@Field("temp") String temp, @Field("dwpt") String dwpt,
                                            @Field("rhum") String rhum, @Field("wdir") String wdir,
                                            @Field("wspd") String wspd, @Field("pres") String pres);

}
