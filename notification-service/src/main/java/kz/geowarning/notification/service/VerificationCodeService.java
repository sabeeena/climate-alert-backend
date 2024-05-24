package kz.geowarning.notification.service;

import kz.geowarning.notification.entity.VerificationCode;
import kz.geowarning.notification.repository.VerificationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class VerificationCodeService {

    @Autowired
    VerificationCodeRepository verificationCodeRepository;

    public void saveVerificationCode(VerificationCode code) {
        verificationCodeRepository.save(code);
    }

    public Optional<VerificationCode> checkVerificationCode(String phoneNumber, String code) {
        return verificationCodeRepository.findActiveCodeWithinTimePeriod(phoneNumber, code,
                LocalDateTime.now().minus(20, ChronoUnit.MINUTES));
    }

    public Optional<VerificationCode> getActiveCode(String phoneNumber) {
        return verificationCodeRepository.findActiveCodeByPhone(phoneNumber);
    }

    public void disableAllOverdueCodesByPhone(String phoneNumber) {
        List<VerificationCode> codes = verificationCodeRepository.getByPhoneNum(phoneNumber);
        for(VerificationCode code : codes) {
            if (code.getCreateDate().isBefore(LocalDateTime.now().minus(20, ChronoUnit.MINUTES))) {
                code.setActive(false);
                verificationCodeRepository.save(code);
            }
        }
    }
}
