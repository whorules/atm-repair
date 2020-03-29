package ru.korovko.atm.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "atm_repair")
@Data
@Accessors(chain = true)
public class AtmRepairEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long caseId;
    private Long atmId;
    private String reason;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String serialNumber;
    private String bankName;
    private String link;
}
