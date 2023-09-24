package kz.geowarning.auth.controller;

import kz.geowarning.auth.entity.User;
import kz.geowarning.auth.service.UserService;
import kz.geowarning.auth.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @DeleteMapping(RestUtils.REST_USER + "/{id}")
    //@PreAuthorize("hasAuthority('admin')")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(RestUtils.REST_USER + "/{id}")
    //@PreAuthorize("hasAnyAuthority('community', 'admin')")
    public User getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

}
