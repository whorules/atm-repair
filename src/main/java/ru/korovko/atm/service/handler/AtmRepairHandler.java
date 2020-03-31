package ru.korovko.atm.service.handler;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.korovko.atm.dto.AtmRepair;
import ru.korovko.atm.dto.LongestRepairComparator;
import ru.korovko.atm.dto.RepairDateComparator;
import ru.korovko.atm.entity.AtmRepairEntity;
import ru.korovko.atm.repository.AtmRepairRepository;
import ru.korovko.atm.service.AtmRepairService;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AtmRepairHandler {

    private final AtmRepairService service;
    private final AtmRepairRepository repository;
    private final ModelMapper modelMapper;

    public List<String> getTopThreeRecurringReasons() {
        List<AtmRepair> repairs = service.getAllAtmRepairEvents();
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
        return entities.stream()
                .sorted(new LongestRepairComparator())
                .limit(3)
                .map(entity -> modelMapper.map(entity, AtmRepair.class))
                .peek(service::changeDateFormatForAtmRepair)
                .collect(Collectors.toList());
    }

    public List<AtmRepair> getAllRecurringRepairs() {
        List<AtmRepairEntity> allRepairs = repository.findAll();
        List<AtmRepair> repeatableRepairs = new ArrayList<>();
        List<List<AtmRepairEntity>> list = getRecurringRepairs(allRepairs);
        for (List<AtmRepairEntity> currentList : list) {
            for (int j = 0; j < currentList.size(); j++) {
                if (j == currentList.size() - 1) {
                    continue;
                }
                AtmRepairEntity currentEntity = currentList.get(j);
                AtmRepairEntity nextEntity = currentList.get(j + 1);
                if (ChronoUnit.DAYS.between(currentEntity.getEndDate(), nextEntity.getStartDate()) <= 15
                        && currentEntity.getReason().equalsIgnoreCase(nextEntity.getReason())) {
                    AtmRepair atmRepair = modelMapper.map(currentEntity, AtmRepair.class);
                    service.changeDateFormatForAtmRepair(atmRepair);
                    repeatableRepairs.add(atmRepair);
                }
            }
        }
        return repeatableRepairs;
    }

    private List<List<AtmRepairEntity>> getRecurringRepairs(List<AtmRepairEntity> entities) {
        Map<String, List<AtmRepairEntity>> map = entities.stream()
                .collect(Collectors.groupingBy(AtmRepairEntity::getAtmId));
        return map.values().stream()
                .filter(atmRepairEntities -> atmRepairEntities.size() > 1)
                .peek(atmRepairEntities -> atmRepairEntities.sort(new RepairDateComparator()))
                .collect(Collectors.toList());
    }
}
