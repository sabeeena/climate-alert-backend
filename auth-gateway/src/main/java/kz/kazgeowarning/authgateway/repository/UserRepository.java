package kz.kazgeowarning.authgateway.repository;

import kz.kazgeowarning.authgateway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select c from User c where c.email = :email")
    Optional<User> findByEmail(String email);

    @Query("select c from User c where c.phoneNumber = :phone and c.active = true")
    Optional<User> findByPhone(String phone);

    @Query("select c from User c where c.id = :id")
    User getById(Long id);

    @Query("select c from User c where c.signupToken = :signupToken")
    User findBySignupToken(String signupToken);

    @Query("select c from User c where c.email = :email and c.active = :active")
    User getByEmailAndActive(String email, Boolean active);

    @Query("select c from User c where c.role = 'ROLE_EMPLOYEE'")
    List<User> getTotalEmployee();
    @Query("select c from User c where c.role = 'ROLE_ADMIN'")
    List<User> getTotalAdmin();
    @Query("select count(c) from User c where c.role = 'ROLE_EMPLOYEE'")
    Long getTotalSizeEmployee();

    @Query("select c from User c where c.active = true and c.notifyEmail = true and c.email is not null and c.locationId is not null")
    List<User> findAllEmailRecipients();

    @Query("select c from User c where c.phoneNumber is not null and c.notifySms = true")
    List<User> findAllSMSReceivers();

    @Modifying
    @Transactional
    @Query("update User c set c.phoneNumber = :phoneNumber where c.email = :email")
    void updatePhoneNumberByEmail(String phoneNumber, String email);
}
