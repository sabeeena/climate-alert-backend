package kz.kazgeowarning.authgateway.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class ResetPasswordDTO {

    private Long userId;
    private String oldPassword;
    private String newPassword;
}
