package kz.kazgeowarning.authgateway.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ROLE_SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    ROLE_EMPLOYEE("ROLE_EMPLOYEE"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_USER("ROLE_USER");

    private final String name;

    public String getName() {
        return name;
    }
}
