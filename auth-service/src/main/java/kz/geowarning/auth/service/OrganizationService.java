package kz.geowarning.auth.service;

import kz.geowarning.auth.entity.Organization;
import kz.geowarning.auth.repository.OrganizationRepository;
import kz.geowarning.common.exceptions.GeneralException;
import kz.geowarning.common.exceptions.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public Organization createOrganization(Organization organization) {
        if (organizationRepository.existsByBinOrFullName(organization.getBin(), organization.getFullName())) {
            throw new GeneralException("Organization Already Exists");
        }
        organization.setCreated(ZonedDateTime.now());
        return organizationRepository.save(organization);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public Organization updateOrganization(Organization organization) {
        Organization formerOrganization = getOrganizationByBin(organization.getBin());

        formerOrganization.setFullName(organization.getFullName());
        formerOrganization.setBin(organization.getBin());
        formerOrganization.setEmail(organization.getEmail());
        formerOrganization.setPhone(organization.getPhone());
        formerOrganization.setAddress(organization.getAddress());
        formerOrganization.setModified(ZonedDateTime.now());
        return organizationRepository.save(formerOrganization);
    }

    public void deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
    }

    @SneakyThrows
    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Organization Not Found"));
    }

    @SneakyThrows
    public Organization getOrganizationByBin(String bin) {
        return organizationRepository.findByBin(bin)
                .orElseThrow(() -> new NotFoundException("Organization Not Found"));
    }

}
