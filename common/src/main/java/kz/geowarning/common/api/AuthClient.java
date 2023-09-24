package kz.geowarning.common.api;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "AuthClient", url = "${app.gateway.url}", path = "/auth/service")
public interface AuthClient {

    // TODO: add methods for data retrieval from auth-service

}
