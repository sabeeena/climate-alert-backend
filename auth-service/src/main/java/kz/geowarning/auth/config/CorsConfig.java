package kz.geowarning.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.addAllowedOrigin("http://localhost:4200"); // Разрешенный источник (фронтенд)
        corsConfig.addAllowedMethod("*"); // Разрешенные HTTP-методы (GET, POST, и т. д.)
        corsConfig.addAllowedHeader("*"); // Разрешенные HTTP-заголовки
        corsConfig.setMaxAge(3600L); // Максимальное время в секундах, на которое браузер может кэшировать CORS ответы.

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig); // Настройка CORS для всех эндпоинтов

        return new CorsWebFilter(source);
    }
}
