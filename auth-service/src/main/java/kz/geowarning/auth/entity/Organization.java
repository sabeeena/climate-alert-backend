package kz.geowarning.auth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import kz.geowarning.auth.util.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organization_seq")
    @SequenceGenerator(name = "organization_seq", allocationSize = 1, sequenceName = "organization_seq")
    private Long id;

    @Column(name = "full_name", unique = true)
    private String fullName;

    @Column(unique = true)
    private String bin;

    private String address;

    private String email;

    private String phone;

    @JsonFormat(pattern = DateUtils.ZONED_DATE_TIME_PATTERN)
    private ZonedDateTime created;

    @JsonFormat(pattern = DateUtils.ZONED_DATE_TIME_PATTERN)
    private ZonedDateTime modified;

    public Organization setId(Long id) {
        this.id = id;
        return this;
    }

}
