package kz.kazgeowarning.authgateway.model.socials;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleUser {
    String id;
    String email;
    String verified_email;
    String picture;
    String name;
    String given_name;
    String family_name;
    String locale;

}
