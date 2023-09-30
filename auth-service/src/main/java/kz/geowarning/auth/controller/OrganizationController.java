package kz.geowarning.auth.controller;

import kz.geowarning.auth.entity.Organization;
import kz.geowarning.auth.service.OrganizationService;
import kz.geowarning.auth.util.RestConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping(RestConstants.REST_ORGANIZATION)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Organization createOrganization(@RequestBody Organization organization) {
        return organizationService.createOrganization(organization);
    }

    @PutMapping(RestConstants.REST_ORGANIZATION)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Organization updateOrganization(@RequestBody Organization organization) {
        return organizationService.updateOrganization(organization);
    }

    @DeleteMapping(RestConstants.REST_ORGANIZATION + "/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(RestConstants.REST_ORGANIZATION + "/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN_PRIVILEGE', 'BASIC_PRIVILEGE')")
    public Organization getOrganizationById(@PathVariable Long id) {
        return organizationService.getOrganizationById(id);
    }

}
