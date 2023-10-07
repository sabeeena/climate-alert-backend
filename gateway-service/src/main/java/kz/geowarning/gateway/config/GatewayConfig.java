package kz.geowarning.gateway.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JCircuitBreakerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Value("${app.gateway.url.auth}")
    private String authUrl;
    @Value("${app.gateway.url.notification}")
    private String notificationUrl;

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_route", r -> r.path("/auth/**")
                        .filters(f -> f.circuitBreaker(c -> c.setFallbackUri("forward:/fallback")))
                        .uri(authUrl))
                .route("notification_route", r -> r.path("/api/notification/**")
                        .filters(f -> f.circuitBreaker(c -> c.setFallbackUri("forward:/fallback")))
                        .uri(notificationUrl))
                .build();
    }

    @Bean
    public ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory(CircuitBreakerRegistry circuitBreakerRegistry) {
        ReactiveResilience4JCircuitBreakerFactory reactiveResilience4JCircuitBreakerFactory = new ReactiveResilience4JCircuitBreakerFactory();
        reactiveResilience4JCircuitBreakerFactory.configureCircuitBreakerRegistry(circuitBreakerRegistry);
        return reactiveResilience4JCircuitBreakerFactory;
    }
}
