package ru.korovko.atm.service.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.korovko.atm.dto.AtmRepair;
import ru.korovko.atm.service.AtmRepairService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AtmRepairHandler {

    private final AtmRepairService service;

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
}
