package ru.korovko.atm.utils;

import ru.korovko.atm.entity.AtmRepairEntity;

import java.util.Comparator;

public class RepairDateComparator implements Comparator<AtmRepairEntity> {

    @Override
    public int compare(AtmRepairEntity firstAtm, AtmRepairEntity secondAtm) {
        return firstAtm.getStartDate().compareTo(secondAtm.getStartDate());
    }
}
