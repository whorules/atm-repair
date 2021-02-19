package ru.korovko.atm.service;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.korovko.atm.dto.AtmRepairResponse;
import ru.korovko.atm.dto.UploadedFilesResponse;
import ru.korovko.atm.mapper.AtmRepairMapper;
import ru.korovko.atm.utils.LongestRepairComparator;
import ru.korovko.atm.utils.RepairDateComparator;
import ru.korovko.atm.entity.AtmRepairEntity;
import ru.korovko.atm.exception.CannotParseDateException;
import ru.korovko.atm.exception.IncorrectFileExtensionException;
import ru.korovko.atm.repository.AtmRepairRepository;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtmRepairService {

    private static final String FORMAT_FROM_FILE = "M/d/yy H:mm";
    private static final SimpleDateFormat DATABASE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    private static final SimpleDateFormat TARGET_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");

    private final AtmRepairRepository repository;
    private final AtmRepairMapper mapper;

    @Transactional
    public UploadedFilesResponse upload(MultipartFile file) throws IOException {
        List<AtmRepairResponse> repairs;
        try (InputStream inputStream = file.getInputStream()) {
            repairs = Poiji.fromExcel(inputStream,
                    determineExcelType(Objects.requireNonNull(file.getOriginalFilename())), AtmRepairResponse.class);
        }
        repairs.stream()
                .map(mapper::toEntity)
                .forEach(repository::save);
        return new UploadedFilesResponse().setCount(repairs.size());
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional(readOnly = true)
    public List<AtmRepairResponse> getAll() {
        List<AtmRepairEntity> entities = repository.findAll();
        return entities.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<String> getTopRecurringReasons(Integer reasonsCount) {
        List<AtmRepairResponse> repairs = getAll();
        Map<String, Long> counts = repairs.stream()
                .collect(Collectors.groupingBy(AtmRepairResponse::getReason, Collectors.counting()));
        return counts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(reasonsCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AtmRepairResponse> getTopLongestRepairs(Integer longestRepairsCount) {
        List<AtmRepairEntity> entities = repository.findAll();
        return entities.stream()
                .sorted(new LongestRepairComparator())
                .limit(longestRepairsCount)
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AtmRepairResponse> getRecurringRepairs() {
        List<AtmRepairEntity> entities = repository.findAll();
        entities.sort(new RepairDateComparator());
        Map<String, AtmRepairEntity> lastRepairDateMap = new HashMap<>();
        List<AtmRepairResponse> recurringRepairs = new ArrayList<>();
        for (AtmRepairEntity entity : entities) {
            AtmRepairEntity entityFromMap = lastRepairDateMap.get(hashFrom(entity));
            if (entityFromMap != null && ChronoUnit.DAYS.between(entityFromMap.getEndDate(), entity.getStartDate()) <= 15) {
                recurringRepairs.add(mapper.toResponse(entityFromMap));
            }
            lastRepairDateMap.put(hashFrom(entity), entity);
        }
        return recurringRepairs;
    }

    private String hashFrom(AtmRepairEntity entity) {
        return entity.getAtmId() + "--" + entity.getReason();
    }

    private PoijiExcelType determineExcelType(String fileName) {
        if (fileName.endsWith(".xls")) {
            return PoijiExcelType.XLS;
        } else if (fileName.endsWith(".xlsx")) {
            return PoijiExcelType.XLSX;
        } else {
            throw new IncorrectFileExtensionException("File format is incorrect");
        }
    }
}