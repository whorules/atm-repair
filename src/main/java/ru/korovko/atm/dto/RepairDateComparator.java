package ru.korovko.atm.dto;

import ru.korovko.atm.entity.AtmRepairEntity;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;

public class RepairDateComparator implements Comparator<AtmRepairEntity> {

    @Override
    public int compare(AtmRepairEntity first, AtmRepairEntity second) {
        long firstRepairTime = getRepairTimeInMilliSeconds(first);
        long secondRepairTime = getRepairTimeInMilliSeconds(second);
        return (int) (secondRepairTime - firstRepairTime);
    }

    private long getRepairTimeInMilliSeconds(AtmRepairEntity entity) {
        LocalDateTime startTime = entity.getStartDate();
        LocalDateTime endTime = entity.getEndDate();
        ZonedDateTime a = startTime.atZone(ZoneId.of("Europe/Moscow"));
        ZonedDateTime b = endTime.atZone(ZoneId.of("Europe/Moscow"));
        long startMillis = a.toInstant().toEpochMilli();
        long endMillis = b.toInstant().toEpochMilli();
        return endMillis - startMillis;
    }
}
