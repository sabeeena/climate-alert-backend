package kz.kazgeowarning.authgateway.security.service;

import kz.kazgeowarning.authgateway.dto.ClientsDTO;
import kz.kazgeowarning.authgateway.model.User;
import kz.kazgeowarning.authgateway.util.PageableCustom;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IUsersService {
    List<User> findAll() throws DataAccessException;
    Long getSizeEmployee();
    Page<User> findAllUsersPaged(Map<String, String> params);
    Pageable pageable(Map<String, String> params);
    User createOrUpdateUser(ClientsDTO clientDTO);
    User findUserByUsername(String username) throws DataAccessException;
    User findBySignupToken(String token) throws DataAccessException;
    User findUserById(Long id) throws DataAccessException;
    Optional<User> findUserByEmail(String id) throws DataAccessException;
    User create(User user);
    User editClient(ClientsDTO clientsDTO);
    Boolean changePassword(Long id, String oldPassword, String newPassword);
    void delete(User user);
    void deleteById(Long id);

    Boolean createPassword(Long id, String newPassword);

    PageableCustom findAllUsersDetail(Map<String, String> params);
}
