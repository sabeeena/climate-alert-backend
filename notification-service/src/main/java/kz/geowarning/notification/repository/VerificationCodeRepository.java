package kz.geowarning.notification.repository;

import kz.geowarning.notification.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

    @Query("SELECT v FROM VerificationCode v " +
            "WHERE v.phoneNumber = :phoneNumber " +
            "AND v.code = :code " +
            "AND v.isActive = true " +
            "AND v.createDate >= :overdueTime")
    // Verification code is only active for 20 mins
    Optional<VerificationCode> findActiveCodeWithinTimePeriod(
            @Param("phoneNumber") String phoneNumber,
            @Param("code") String code,
            @Param("overdueTime") LocalDateTime time);

    @Query("SELECT v FROM VerificationCode v " +
            "WHERE v.phoneNumber = :phoneNumber")
    List<VerificationCode> getByPhoneNum(String phoneNumber);

    @Query("SELECT v FROM VerificationCode v " +
            "WHERE v.phoneNumber = :phoneNumber " +
            "AND v.isActive = true")
    Optional<VerificationCode> findActiveCodeByPhone(String phoneNumber);
}
