package kz.geowarning.data.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressInfoDTO {
    private String addressLine;
    private String adminDistrict;
    private String adminDistrict2;
    private String countryRegion;
    private String formattedAddress;
    private String locality;
    private String postalCode;
}
