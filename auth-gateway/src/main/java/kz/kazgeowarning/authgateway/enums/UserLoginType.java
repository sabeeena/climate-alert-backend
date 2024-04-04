package kz.kazgeowarning.authgateway.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserLoginType {
    facebook("facebook"),
    google("google"),
    origin("origin");

    private final String name;

    public String getName() {
        return name;
    }
}
