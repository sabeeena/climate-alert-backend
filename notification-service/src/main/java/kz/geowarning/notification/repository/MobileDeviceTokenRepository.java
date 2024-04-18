package kz.geowarning.notification.repository;

import kz.geowarning.notification.entity.MobileDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MobileDeviceTokenRepository extends JpaRepository<MobileDeviceToken, Long> {
    MobileDeviceToken findByDeviceToken(String deviceToken);
}
