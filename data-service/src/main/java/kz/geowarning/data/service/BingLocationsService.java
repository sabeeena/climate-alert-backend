package kz.geowarning.data.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.geowarning.data.entity.dto.AddressInfoDTO;
import kz.geowarning.data.service.retrofit.BingMapsService;
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
public class BingLocationsService {

    @Value("${api.bing.url}")
    private String bingApiUrl;

    @Value("${api.bing.key}")
    private String bingApiKey;

    private BingMapsService bingMapsService;

    @PostConstruct
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(bingApiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        bingMapsService = retrofit.create(BingMapsService.class);
    }

    public AddressInfoDTO getAddressInfoByCoordinates(String latitude, String longitude) throws IOException {
        Call<ResponseBody> retrofitCall = bingMapsService.getLocationInfo(latitude, longitude, "json", bingApiKey);

        Response<ResponseBody> response = retrofitCall.execute();

        if (!response.isSuccessful()) {
            throw new IOException(response.errorBody() != null
                    ? response.errorBody().string() : "Unknown error");
        }

        ResponseBody responseBody = response.body();
        String jsonString = responseBody.string();

        return convertFromJson(jsonString);
    }

    private static AddressInfoDTO convertFromJson(String jsonString) {
        AddressInfoDTO addressInfo = new AddressInfoDTO();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode readJson = mapper.readTree(jsonString);
            JsonNode resourceSets = readJson.get("resourceSets");

            JsonNode resources = resourceSets.get(0).get("resources");
            JsonNode addressNode = resources.get(0).get("address");

            addressInfo.setAddressLine(String.valueOf(addressNode.get("addressLine")).replaceAll("^\\s*\"|\"\\s*$", "").trim());
            addressInfo.setAdminDistrict(String.valueOf(addressNode.get("adminDistrict")).replaceAll("^\\s*\"|\"\\s*$", "").trim());
            addressInfo.setAdminDistrict2(String.valueOf(addressNode.get("adminDistrict2")).replaceAll("^\\s*\"|\"\\s*$", "").trim());
            addressInfo.setCountryRegion(String.valueOf(addressNode.get("countryRegion")).replaceAll("^\\s*\"|\"\\s*$", "").trim());
            addressInfo.setFormattedAddress(String.valueOf(addressNode.get("formattedAddress")).replaceAll("^\\s*\"|\"\\s*$", "").trim());
            addressInfo.setLocality(String.valueOf(addressNode.get("locality")).replaceAll("^\\s*\"|\"\\s*$", "").trim());
            addressInfo.setPostalCode(String.valueOf(addressNode.get("postalCode")).replaceAll("^\\s*\"|\"\\s*$", "").trim());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return addressInfo;
    }
}
