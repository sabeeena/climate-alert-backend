package kz.geowarning.auth.controller;

import kz.geowarning.auth.entity.Organization;
import kz.geowarning.auth.service.OrganizationService;
import kz.geowarning.auth.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @PostMapping(RestUtils.REST_ORGANIZATION)
    //@PreAuthorize("hasAuthority('admin')")
    public Organization createOrganization(@RequestBody Organization organization) {
        return organizationService.createOrganization(organization);
    }

    @PutMapping(RestUtils.REST_ORGANIZATION)
    //@PreAuthorize("hasAuthority('admin')")
    public Organization updateOrganization(@RequestBody Organization organization) {
        return organizationService.updateOrganization(organization);
    }

    @DeleteMapping(RestUtils.REST_ORGANIZATION + "/{id}")
    //@PreAuthorize("hasAuthority('admin')")
    public ResponseEntity deleteOrganization(@PathVariable Long id) {
        organizationService.deleteOrganization(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(RestUtils.REST_ORGANIZATION + "/{id}")
    //@PreAuthorize("hasAuthority('admin')")
    public Organization getOrganizationById(@PathVariable Long id) {
        return organizationService.getOrganizationById(id);
    }

}
