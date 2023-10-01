package kz.geowarning.auth.config;

import io.swagger.annotations.ApiParam;
import lombok.Getter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.awt.print.Pageable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(Arrays.asList("application/json"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .securitySchemes(Collections.singletonList(new ApiKey("JWT", "Authorization", "header")))
                .securityContexts(Collections.singletonList(SecurityContext.builder()
                        .securityReferences(defaultAuth()).build()))
                .forCodeGeneration(true)
                .genericModelSubstitutes(ResponseEntity.class, Optional.class, CompletableFuture.class)
                .ignoredParameterTypes(java.sql.Date.class)
                .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
                .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
                .directModelSubstitute(Pageable.class, SwaggerPageable.class)
                .select()
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("GeoWarning API Documentation")
                .description("Auth Service")
                .version("1.0.0")
                .build();
    }

    @Getter
    private static class SwaggerPageable {

        @ApiParam(value = "Number of records per page", example = "0")
        private Integer size;

        @ApiParam(value = "Results page you want to retrieve (0..N)", example = "0")
        private Integer page;

        @ApiParam(value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
        private String sort;

    }
}