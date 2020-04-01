package ru.korovko.atm.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "atm_repair")
@Data
@Accessors(chain = true)
public class AtmRepairEntity {

    @Id
    private Long caseId;
    private String atmId;
    private String reason;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String serialNumber;
    private String bankName;
    private String channel;
}
