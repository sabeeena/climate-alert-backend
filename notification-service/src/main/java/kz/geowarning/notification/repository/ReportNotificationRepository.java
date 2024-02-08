package kz.geowarning.notification.repository;

import kz.geowarning.notification.entity.ReportNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportNotificationRepository extends JpaRepository<ReportNotification, Long> {
}
