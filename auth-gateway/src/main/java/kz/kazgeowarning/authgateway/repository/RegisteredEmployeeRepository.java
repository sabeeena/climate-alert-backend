package kz.kazgeowarning.authgateway.repository;

import kz.kazgeowarning.authgateway.model.RegisteredEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegisteredEmployeeRepository extends JpaRepository<RegisteredEmployee, Long> {
}
