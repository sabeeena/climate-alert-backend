package kz.kazgeowarning.authgateway;

import kz.kazgeowarning.authgateway.config.ApplicationSwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@EnableConfigurationProperties
@Import(ApplicationSwaggerConfig.class)
public class AuthGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthGatewayApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public ServerCodecConfigurer serverCodecConfigurer() {
		return ServerCodecConfigurer.create();
	}
}

