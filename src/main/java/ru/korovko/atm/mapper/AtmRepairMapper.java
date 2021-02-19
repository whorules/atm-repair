package ru.korovko.atm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.korovko.atm.dto.AtmRepairResponse;
import ru.korovko.atm.entity.AtmRepairEntity;
import ru.korovko.atm.exception.CannotParseDateException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(componentModel = "spring")
public interface AtmRepairMapper {

    String DATE_FORMAT_FROM_FILE = "M/d/yy H:mm";

    AtmRepairResponse toResponse(AtmRepairEntity entity);

    @Mapping(target = "startDate", source = "response.startDate", qualifiedByName = "convertToDatabaseFormat")
    @Mapping(target = "endDate", qualifiedByName = "convertToDatabaseFormat")
    AtmRepairEntity toEntity(AtmRepairResponse response);

    @Named("convertToDatabaseFormat")
    default LocalDateTime parseStringToLocalDateTime(String date) {
        LocalDateTime parse;
        try {
            parse = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_FORMAT_FROM_FILE));
        } catch (DateTimeParseException e) {
            throw new CannotParseDateException(e.getMessage(), e);
        }
        return parse;
    }
}
