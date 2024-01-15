package kz.kazgeowarning.authgateway.repository;

import kz.kazgeowarning.authgateway.model.RegisteredEmployee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

public interface RegisteredEmployeeRepository extends JpaRepository<RegisteredEmployee, Long> {
    @Query(nativeQuery = true, value = "select * from registered_employee where admin_id = " +
            "(select admin_id from registered_employee where employee_email=:email)")
    Set<RegisteredEmployee> findAllEmployeeByEEmail(String email);
}
