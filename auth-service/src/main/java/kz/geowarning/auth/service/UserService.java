package kz.geowarning.auth.service;

import kz.geowarning.auth.entity.User;
import kz.geowarning.auth.repository.UserRepository;
import kz.geowarning.common.dto.UserDto;
import kz.geowarning.common.exceptions.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // TODO: create, update methods, when done with authorization (once encryption is determined)

    public void deleteUser(Long id) {
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

    private UserDto userToDTO(User user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getOrganization().getId(),
                user.getJobTitle(),
                user.getBirthDate()
        );
    }

}
