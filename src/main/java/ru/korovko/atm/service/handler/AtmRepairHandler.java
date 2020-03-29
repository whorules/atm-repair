package ru.korovko.atm.service.handler;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.korovko.atm.dto.AtmRepair;
import ru.korovko.atm.dto.RepairDateComparator;
import ru.korovko.atm.entity.AtmRepairEntity;
import ru.korovko.atm.repository.AtmRepairRepository;
import ru.korovko.atm.service.AtmRepairService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AtmRepairHandler {

    private final AtmRepairService service;
    private final AtmRepairRepository repository;
    @Autowired
    ModelMapper modelMapper;

    public List<String> getTopThreeRepeatableReasons() {
        List<AtmRepair> repairs = service.getAllData();
        Map<String, Long> count = repairs.stream()
                .collect(Collectors.groupingBy(AtmRepair::getReason, Collectors.counting()));
        return count.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<AtmRepair> getTopThreeLongestRepairs() {
        RepairDateComparator comparator = new RepairDateComparator();
        List<AtmRepairEntity> entities = repository.findAll();
        return entities.stream()
                .sorted(comparator)
                .limit(3)
                .map(entity -> modelMapper.map(entity, AtmRepair.class))
                .peek(service::changeDateForAtmRepair)
                .collect(Collectors.toList());
    }

    private List<AtmRepair> getRepeatableRepairs() {
        return null;
    }
}
