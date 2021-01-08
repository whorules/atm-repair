package ru.korovko.atm.utils;

import ru.korovko.atm.entity.AtmRepairEntity;

import java.time.temporal.ChronoUnit;
import java.util.Comparator;

public class LongestRepairComparator implements Comparator<AtmRepairEntity> {

    @Override
    public int compare(AtmRepairEntity firstAtm, AtmRepairEntity secondAtm) {
        long firstAtmRepairmentDuration = ChronoUnit.SECONDS.between(firstAtm.getStartDate(), firstAtm.getEndDate());
        long secondAtmRepairmentDuration = ChronoUnit.SECONDS.between(secondAtm.getStartDate(), secondAtm.getEndDate());
        return (int) (secondAtmRepairmentDuration - firstAtmRepairmentDuration);
    }
}
