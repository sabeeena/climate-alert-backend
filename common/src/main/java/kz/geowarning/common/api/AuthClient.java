package kz.geowarning.common.api;

import kz.geowarning.common.dto.OrganizationDto;
import kz.geowarning.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "AuthClient", url = "${app.gateway.url}", path = "/auth/service")
public interface AuthClient {

    static final String REST_USER = "/user";
    static final String REST_ORGANIZATION = "/organization";

    @GetMapping(path = REST_USER, params = "username")
    UserDto getUserByUsername(@RequestParam String username);

    @GetMapping(path = REST_ORGANIZATION, params = "id")
    OrganizationDto getOrganizationById(@RequestParam Long id);

}
