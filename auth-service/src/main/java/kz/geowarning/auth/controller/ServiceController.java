package kz.geowarning.auth.controller;

import kz.geowarning.auth.service.OrganizationService;
import kz.geowarning.auth.service.UserService;
import kz.geowarning.common.api.AuthClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/service")
public class ServiceController implements AuthClient {

    @Autowired
    private UserService userService;

    @Autowired
    private OrganizationService organizationService;

    // TODO: rests
}
