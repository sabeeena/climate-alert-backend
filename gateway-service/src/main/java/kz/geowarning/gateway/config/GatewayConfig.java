package kz.geowarning.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class GatewayConfig {

    @Value("${app.gateway.url.auth}")
    private String authUrl;
    @Value("${app.gateway.url.notification}")
    private String notificationUrl;
    @Value("${app.gateway.url.data}")
    private String dataUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_route", r -> r.path("/api/auth/**")
                        .filters(f -> f.circuitBreaker(c -> c.setFallbackUri("forward:/fallback")))
                        .uri(authUrl))
                .route("notification_route", r -> r.path("/api/notification/**")
                        .filters(f -> f.circuitBreaker(c -> c.setFallbackUri("forward:/fallback")))
                        .uri(notificationUrl))
                .route("data_route", r -> r.path("/api/data/**")
                        .filters(f -> f.circuitBreaker(c -> c.setFallbackUri("forward:/fallback")))
                        .uri(dataUrl))
                .build();
    }

    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(15)).build())
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .build());
    }
}
