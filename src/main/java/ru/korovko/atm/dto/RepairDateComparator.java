package ru.korovko.atm.dto;

import ru.korovko.atm.entity.AtmRepairEntity;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;

public class RepairDateComparator implements Comparator<AtmRepairEntity> {

    @Override
    public int compare(AtmRepairEntity first, AtmRepairEntity second) {
        long firstDate = ChronoUnit.SECONDS.between(first.getStartDate(), first.getEndDate());
        long secondDate = ChronoUnit.SECONDS.between(second.getStartDate(), second.getEndDate());
        return (int) (secondDate - firstDate);
    }
}
