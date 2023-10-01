package kz.geowarning.auth.service;

import kz.geowarning.common.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@Slf4j
public class TestDataService {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void loadTestData() {
        try {
            // Admin users
            userService.createUser(new UserDto(null, "billie", passwordEncoder.encode("wherearetheavocados"),
                    "Billie", "Eilish", null, "billie@mail.com", "+16251781917", null,
                    roleService.getRoleByRoleCode("ADMIN").getId(), null, LocalDate.of(2001, 12, 18)));

            userService.createUser(new UserDto(null, "taytay", passwordEncoder.encode("johnmayersucks13"),
                    "Taylor", "Swift", null, "ts@mail.com", "+19891331112", null,
                    roleService.getRoleByRoleCode("ADMIN").getId(), null, LocalDate.of(1989, 12, 13)));

            // Supervisor users
            userService.createUser(new UserDto(null, "olivia", passwordEncoder.encode("spicypisces"),
                    "Olivia", "Rodrigo", null, "liv@mail.com", "+17771682255", null,
                    roleService.getRoleByRoleCode("SUPERVISOR").getId(), null, LocalDate.of(2003, 2, 20)));

            userService.createUser(new UserDto(null, "sabrina", passwordEncoder.encode("howtosendanemail11"),
                    "Sabrina", "Carpenter", null, "sab@mail.com", "+12191756336", null,
                    roleService.getRoleByRoleCode("SUPERVISOR").getId(), null, LocalDate.of(1999, 5, 11)));

            // Simple users
            userService.createUser(new UserDto(null, "alex", passwordEncoder.encode("teddypicker18"),
                    "Alexander", "Turner", "David", "am@mail.com", "+13542961212", null,
                    roleService.getRoleByRoleCode("USER").getId(), null, LocalDate.of(1986, 1, 6)));

            userService.createUser(new UserDto(null, "hozier", passwordEncoder.encode("francesca17"),
                    "Andrew", "Hozier-Byrne", "John", "hozier@mail.com", "+11155694823", null,
                    roleService.getRoleByRoleCode("USER").getId(), null, LocalDate.of(1990, 3, 17)));

            log.info("Loaded test data");
        } catch (Exception e) {
            log.info("Test data already loaded, skipping");
        }
    }

}
