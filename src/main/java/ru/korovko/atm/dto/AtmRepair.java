package ru.korovko.atm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.poiji.annotation.ExcelCell;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AtmRepair {

    @ExcelCell(0)
    private Long caseId;
    @ExcelCell(1)
    private String atmId;
    @ExcelCell(2)
    private String reason;
    @ExcelCell(3)
    private String startDate;
    @ExcelCell(4)
    private String endDate;
    @ExcelCell(5)
    private String serialNumber;
    @ExcelCell(6)
    private String bankName;
    @ExcelCell(7)
    private String channel;
    private Integer uploadedFilesCount;
}
