package ru.korovko.atm.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

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
    private Date startDate;
    private Date endDate;
    private String serialNumber;
    private String bankName;
    private String link;
}
