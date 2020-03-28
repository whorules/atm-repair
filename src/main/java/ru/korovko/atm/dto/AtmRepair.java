package ru.korovko.atm.dto;

import com.poiji.annotation.ExcelCell;
import jdk.vm.ci.meta.Local;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Accessors(chain = true)
public class AtmRepair {

    private Long id;
    @ExcelCell(0)
    private Long caseId;
    @ExcelCell(1)
    private Long atmId;
    @ExcelCell(2)
    private String reason;
    @ExcelCell(3)
    private Date startDate;
    @ExcelCell(4)
    private Date endDate;
    @ExcelCell(5)
    private String serialNumber;
    @ExcelCell(6)
    private String bankName;
    @ExcelCell(7)
    private String link;
}
