package kz.geowarning.auth;

import kz.geowarning.auth.service.TestDataService;
import kz.geowarning.common.exceptions.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EnableSwagger2
@Slf4j
public class AuthApplication implements ApplicationRunner {

	@Autowired
	private TestDataService testDataService;

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		log.info("AuthServiceApplication initialize");
		testDataService.loadTestData();
	}

}
