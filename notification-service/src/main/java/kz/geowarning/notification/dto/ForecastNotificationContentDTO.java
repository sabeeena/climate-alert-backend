package kz.geowarning.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ForecastNotificationContentDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String locationName;
    private String level;

}
