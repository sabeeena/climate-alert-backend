package kz.geowarning.auth.service;

import kz.geowarning.auth.entity.Organization;
import kz.geowarning.auth.entity.Role;
import kz.geowarning.auth.entity.User;
import kz.geowarning.auth.repository.UserRepository;
import kz.geowarning.common.dto.UserDto;
import kz.geowarning.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private OrganizationService organizationService;

    private final PasswordEncoder passwordEncoder;

    public void deleteUser(Long id) {
        User updUser = getUserById(id);
        updUser.setModified(ZonedDateTime.now());
        userRepository.save(updUser);
        userRepository.deleteById(id);
    }

    @SneakyThrows
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @SneakyThrows
    public UserDto getUserDtoByUsername(String username) {
        return userRepository.findByUsername(username).map(this::userToDTO).orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @SneakyThrows
    public User createUser(UserDto user) {
        if (userRepository.existsByUsername(user.getUsername()) ||
                userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("User Already Exists");
        }

        Role role = null;
        Organization organization = null;

        if (user.getRoleId() != null) {
            role = roleService.getRoleById(user.getRoleId());
        }

        if (user.getOrganizationId() != null) {
            organization = organizationService.getOrganizationById(user.getOrganizationId());
        }

        User newUser = User.builder()
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .isEnabled(true)
                .isDeleted(false)
                .role(role)
                .organization(organization)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .fullName(String.format("%s %s %s",
                        Optional.ofNullable(user.getLastName()).orElse(""),
                        Optional.ofNullable(user.getFirstName()).orElse(""),
                        Optional.ofNullable(user.getMiddleName()).orElse("")
                                .trim()
                ))
                .birthDate(user.getBirthDate())
                .email(user.getEmail())
                .phone(user.getPhone())
                .created(ZonedDateTime.now())
                .isEmailVerified(false)
                .isPhoneVerified(false)
                .build();

        return userRepository.save(newUser);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public User updateUser(UserDto user) {
        User formerUser = getUserById(user.getId());
        Role role = null;
        Organization organization = null;

        if (user.getRoleId() != null) {
            role = roleService.getRoleById(user.getRoleId());
        }

        if (user.getOrganizationId() != null) {
            organization = organizationService.getOrganizationById(user.getOrganizationId());
        }

        formerUser.setUsername(user.getUsername());
        formerUser.setPassword(passwordEncoder.encode(user.getPassword()));
        formerUser.setRole(role);
        formerUser.setOrganization(organization);
        formerUser.setJobTitle(user.getJobTitle());
        formerUser.setFirstName(user.getFirstName());
        formerUser.setLastName(user.getLastName());
        formerUser.setMiddleName(user.getMiddleName());
        formerUser.setFullName(String.format("%s %s %s",
                Optional.ofNullable(user.getLastName()).orElse(""),
                Optional.ofNullable(user.getFirstName()).orElse(""),
                Optional.ofNullable(user.getMiddleName()).orElse("")
                        .trim()
        ));
        formerUser.setBirthDate(user.getBirthDate());
        formerUser.setEmail(user.getEmail());
        formerUser.setPhone(user.getPhone());
        formerUser.setModified(ZonedDateTime.now());

        return userRepository.save(formerUser);
    }

    private UserDto userToDTO(User user) {
        Long organizationId = null;
        if (user.getOrganization() != null) {
            organizationId = user.getOrganization().getId();
        }
        Long roleId = null;
        if (user.getRole() != null) {
            roleId = user.getRole().getId();
        }
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getPhone(),
                organizationId,
                roleId,
                user.getJobTitle(),
                user.getBirthDate()
        );
    }

}
