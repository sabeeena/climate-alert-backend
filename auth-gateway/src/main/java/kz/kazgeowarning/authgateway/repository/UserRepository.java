package kz.kazgeowarning.authgateway.repository;

import kz.kazgeowarning.authgateway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select c from User c where c.email = :email")
    Optional<User> findByEmail(String email);

    @Query("select c from User c where c.id = :id")
    User getById(Long id);

    @Query("select c from User c where c.signupToken = :signupToken")
    User findBySignupToken(String signupToken);

    @Query("select c from User c where c.email = :email and c.active = :active")
    User getByEmailAndActive(String email, Boolean active);

    @Query("select c from User c where c.role = 'ROLE_EMPLOYEE'")
    Optional<User> getTotalEmployee();
    @Query("select count(c) from User c where c.role = 'ROLE_EMPLOYEE'")
    Long getTotalSizeEmployee();

}
