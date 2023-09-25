package kz.geowarning.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDto {

    private Long id;
    private String fullName;
    private String bin;
    private String address;
    private String email;
    private String phone;

}
