package kz.geowarning.auth.service;

import kz.geowarning.auth.entity.Role;
import kz.geowarning.auth.repository.RoleRepository;
import kz.geowarning.common.exceptions.GeneralException;
import kz.geowarning.common.exceptions.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @SneakyThrows
    public Role createRole(@RequestBody Role role) {
        if(roleRepository.existsByRoleCode(role.getRoleCode())) {
            throw new RuntimeException("Role Already Exists");
        }
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @SneakyThrows
    public Role getRoleByRoleCode(String roleCode) {
        return roleRepository.getRoleByRoleCode(roleCode)
                .orElseThrow(() -> new NotFoundException("Role Not Found"));
    }

    @SneakyThrows
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Role Not Found"));
    }

    public void deleteRoleById(Long id) {
        roleRepository.deleteById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public Role updateRole(Role role) {
        Role formerRole = getRoleById(role.getId());

        formerRole.setName(role.getName());
        formerRole.setRoleCode(role.getRoleCode());
        formerRole.setPrivileges(role.getPrivileges());
        return roleRepository.save(formerRole);
    }
}
