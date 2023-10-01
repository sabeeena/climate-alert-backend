package kz.geowarning.auth.controller;

import kz.geowarning.auth.entity.Privilege;
import kz.geowarning.auth.service.PrivilegeService;
import kz.geowarning.auth.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class PrivilegeController {

    @Autowired
    private PrivilegeService privilegeService;

    @PostMapping(RestConstants.REST_PRIVILEGE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Privilege createPrivilege(@RequestBody Privilege privilege) {
        return privilegeService.createPrivilege(privilege);
    }

    @PutMapping(RestConstants.REST_PRIVILEGE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Privilege updatePrivilege(@RequestBody Privilege privilege) {
        return privilegeService.updatePrivilege(privilege);
    }

    @DeleteMapping(RestConstants.REST_PRIVILEGE + "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deletePrivilege(@PathVariable Long id) {
        privilegeService.deletePrivilegeById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(RestConstants.REST_PRIVILEGE + "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_PRIVILEGE', 'BASIC_PRIVILEGE')")
    public Privilege getPrivilegeById(@PathVariable Long id) {
        return privilegeService.getPrivilegeById(id);
    }
}
