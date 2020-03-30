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
        List<AtmRepairEntity> entities = repository.findAll();
        List<AtmRepair> list =
         entities.stream()
                .sorted(new RepairDateComparator())
                .map(entity -> modelMapper.map(entity, AtmRepair.class))
                 .peek(service::changeDateForAtmRepair)
                 .limit(3)
                 .collect(Collectors.toList());
    return list;
    }

    private List<AtmRepair> getRepeatableRepairs() {
        return null;
    }
}
