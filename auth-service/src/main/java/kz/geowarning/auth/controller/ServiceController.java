package kz.geowarning.auth.controller;

import kz.geowarning.auth.service.OrganizationService;
import kz.geowarning.auth.service.UserService;
import kz.geowarning.common.api.AuthClient;
import kz.geowarning.common.dto.OrganizationDto;
import kz.geowarning.common.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class ServiceController implements AuthClient {

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    @GetMapping(path = REST_USER, params = "username")
    public UserDto getUserByUsername(String username) {
        return userService.getUserDtoByUsername(username);
    }

    @Override
    public OrganizationDto getOrganizationById(Long id) {
        return organizationService.getOrganizationDtoById(id);
    }

}
