package ru.korovko.atm.dto;

import ru.korovko.atm.entity.AtmRepairEntity;

import java.util.Comparator;

public class RepairDateComparator implements Comparator<AtmRepairEntity> {

    @Override
    public int compare(AtmRepairEntity o1, AtmRepairEntity o2) {
        return o1.getStartDate().compareTo(o2.getStartDate());
    }
}
