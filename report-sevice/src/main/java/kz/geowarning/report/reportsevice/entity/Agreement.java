package kz.geowarning.report.reportsevice.entity;

import java.io.Serializable;

public interface Agreement<T extends Serializable> extends Serializable {

    T agreed();

    Long getId();

    void setId(Long id);

    Long getAgreementId();

    void setAgreementId(Long agreementId);
}
