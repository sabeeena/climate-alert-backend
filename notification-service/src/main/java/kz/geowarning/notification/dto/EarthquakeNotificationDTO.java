package kz.geowarning.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EarthquakeNotificationDTO {
    private String phoneNumber;
    private String email;
    private String firstName;
    private String lastName;
    private String locationName;
    private String magnitude;
    private String time;
    private String language;
}
