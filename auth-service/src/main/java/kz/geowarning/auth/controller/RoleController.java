package kz.geowarning.auth.controller;

import kz.geowarning.auth.entity.Role;
import kz.geowarning.auth.service.RoleService;
import kz.geowarning.auth.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping(RestConstants.REST_ROLE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @PutMapping(RestConstants.REST_ROLE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Role updateRole(@RequestBody Role role) {
        return roleService.updateRole(role);
    }

    @DeleteMapping(RestConstants.REST_ROLE + "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteRole(@PathVariable Long id) {
        roleService.deleteRoleById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(RestConstants.REST_ROLE + "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_PRIVILEGE', 'BASIC_PRIVILEGE')")
    public Role getRoleById(@PathVariable Long id) {
        return roleService.getRoleById(id);
    }
}
