package kz.geowarning.report.reportsevice.repository;

import kz.geowarning.report.reportsevice.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatusRepository  extends JpaRepository<Status, Long> {

}
