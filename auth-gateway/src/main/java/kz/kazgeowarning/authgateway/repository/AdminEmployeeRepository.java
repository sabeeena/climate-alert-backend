package kz.kazgeowarning.authgateway.repository;

import kz.kazgeowarning.authgateway.model.AdminEmployee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminEmployeeRepository extends JpaRepository<AdminEmployee, Long> {
    AdminEmployee findByAdminEmail(String adminEmail);
}
