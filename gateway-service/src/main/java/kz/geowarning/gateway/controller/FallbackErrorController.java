package kz.geowarning.gateway.controller;

import kz.geowarning.common.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackErrorController {

    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @RequestMapping("/fallback")
    public Mono<ErrorResponse> fallback() {
        ErrorResponse fallbackError = ErrorResponse.builder()
                .errorMessage("Service is temporarily unavailable. Please try again later.")
                .build();
        return Mono.just(fallbackError);
    }

}
