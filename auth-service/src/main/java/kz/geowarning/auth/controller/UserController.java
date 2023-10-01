package kz.geowarning.auth.controller;

import kz.geowarning.auth.entity.User;
import kz.geowarning.auth.service.UserService;
import kz.geowarning.auth.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping(RestConstants.REST_USER + "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping(RestConstants.REST_USER)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping(RestConstants.REST_USER)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping(RestConstants.REST_USER + "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_PRIVILEGE', 'BASIC_PRIVILEGE')")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

}
