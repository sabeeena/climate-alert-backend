package kz.geowarning.auth.config.security;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"kz.geowarning.common.api"})
public class FeignConfig extends FeignClientsConfiguration {

    // TODO: Configure

}
