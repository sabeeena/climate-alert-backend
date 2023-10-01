package kz.geowarning.auth.service;

import kz.geowarning.auth.entity.User;
import kz.geowarning.auth.repository.UserRepository;
import kz.geowarning.common.dto.UserDto;
import kz.geowarning.common.exceptions.GeneralException;
import kz.geowarning.common.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

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

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @SneakyThrows
    public UserDto getUserDtoByUsername(String username) {
        return userRepository.findByUsername(username).map(this::userToDTO).orElseThrow(() -> new NotFoundException("User Not Found"));
    }

    @SneakyThrows
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername()) ||
                userRepository.existsByEmail(user.getEmail())) {
            throw new GeneralException("User Already Exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setCreated(ZonedDateTime.now());
        user.setFullName(String.format("%s %s %s",
                Optional.ofNullable(user.getLastName()).orElse(""),
                Optional.ofNullable(user.getFirstName()).orElse(""),
                Optional.ofNullable(user.getMiddleName()).orElse("")
        ));
        user.setEmailVerified(false);
        user.setPhoneVerified(false);
        user.setDeleted(false);

        return userRepository.save(user);
    }

    @Transactional(rollbackFor = Exception.class)
    @SneakyThrows
    public User updateUser(User user) {
        User formerUser = getUserById(user.getId());

        formerUser.setUsername(user.getUsername());
        formerUser.setPassword(passwordEncoder.encode(user.getPassword()));
        formerUser.setRole(user.getRole());
        formerUser.setOrganization(user.getOrganization());
        formerUser.setJobTitle(user.getJobTitle());
        formerUser.setFirstName(user.getFirstName());
        formerUser.setLastName(user.getLastName());
        formerUser.setMiddleName(user.getMiddleName());
        formerUser.setFullName(String.format("%s %s %s",
                Optional.ofNullable(user.getLastName()).orElse(""),
                Optional.ofNullable(user.getFirstName()).orElse(""),
                Optional.ofNullable(user.getMiddleName()).orElse("")
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
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                organizationId,
                user.getJobTitle(),
                user.getBirthDate()
        );
    }

}
