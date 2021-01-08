package ru.korovko.atm.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UploadedFilesResponse {

    private Integer count;
}
