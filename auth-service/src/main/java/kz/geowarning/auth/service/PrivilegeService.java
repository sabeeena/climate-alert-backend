package kz.geowarning.auth.service;

import kz.geowarning.auth.entity.Privilege;
import kz.geowarning.auth.repository.PrivilegeRepository;
import kz.geowarning.common.exceptions.GeneralException;
import kz.geowarning.common.exceptions.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class PrivilegeService {

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @SneakyThrows
    public Privilege createPrivilege(@RequestBody Privilege privilege) {
        if(privilegeRepository.existsByPrivilegeCode(privilege.getPrivilegeCode())) {
            throw new GeneralException("Privilege Already Exists");
        }
        return privilegeRepository.save(privilege);
    }

    public List<Privilege> getAllPrivileges() {
        return privilegeRepository.findAll();
    }

    @SneakyThrows
    public Privilege getPrivilegeById(Long id) {
        return privilegeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Privilege Not Found"));
    }

    public void deletePrivilegeById(Long id) {
        privilegeRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public Privilege updatePrivilege(Privilege privilege) {
        Privilege formerPrivilege = getPrivilegeById(privilege.getId());

        formerPrivilege.setName(privilege.getName());
        formerPrivilege.setPrivilegeCode(privilege.getPrivilegeCode());
        return privilegeRepository.save(formerPrivilege);
    }

}
