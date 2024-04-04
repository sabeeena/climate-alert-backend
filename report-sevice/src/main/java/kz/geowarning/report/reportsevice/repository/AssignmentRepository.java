package kz.geowarning.report.reportsevice.repository;

import kz.geowarning.report.reportsevice.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByUserIncoming(String email);
}
