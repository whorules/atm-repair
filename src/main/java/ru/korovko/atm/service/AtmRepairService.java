package ru.korovko.atm.service;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.korovko.atm.dto.AtmRepair;
import ru.korovko.atm.dto.LongestRepairComparator;
import ru.korovko.atm.dto.RepairDateComparator;
import ru.korovko.atm.entity.AtmRepairEntity;
import ru.korovko.atm.exception.CannotParseDateException;
import ru.korovko.atm.exception.IncorrectFileExtensionException;
import ru.korovko.atm.repository.AtmRepairRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtmRepairService {

    private static final String FORMAT_FROM_FILE = "M/d/yy H:mm";
    private static final SimpleDateFormat FORMAT_FROM_DATABASE = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    private static final SimpleDateFormat TARGET_FORMAT = new SimpleDateFormat("dd-MM-yyyy HH:mm");
    private final ModelMapper modelMapper;
    private final AtmRepairRepository repository;

    public AtmRepair uploadExcelFileToDatabase(MultipartFile file) throws IOException {
        List<AtmRepair> repairs = Poiji.fromExcel(file.getInputStream(),
                determineExcelType(Objects.requireNonNull(file.getOriginalFilename())), AtmRepair.class);
        saveDataToDatabase(repairs);
        return new AtmRepair()
                .setUploadedFilesCount(repairs.size());
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public List<AtmRepair> getAllAtmRepairs() {
        List<AtmRepairEntity> entities = repository.findAll();
        return entities.stream()
                .map(entity -> modelMapper.map(entity, AtmRepair.class))
                .peek(this::changeDateFormatForAtmRepair)
                .collect(Collectors.toList());
    }

    public List<String> getTopRecurringReasons(Integer reasonsCount) {
        List<AtmRepair> repairs = getAllAtmRepairs();
        Map<String, Long> counts = repairs.stream()
                .collect(Collectors.groupingBy(AtmRepair::getReason, Collectors.counting()));
        return counts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(reasonsCount)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public List<AtmRepair> getTopLongestRepairs(Integer longestRepairsCount) {
        List<AtmRepairEntity> entities = repository.findAll();
        return entities.stream()
                .sorted(new LongestRepairComparator())
                .limit(longestRepairsCount)
                .map(entity -> modelMapper.map(entity, AtmRepair.class))
                .peek(this::changeDateFormatForAtmRepair)
                .collect(Collectors.toList());
    }

    public List<AtmRepair> getRecurringRepairs() {
        List<AtmRepairEntity> entities = repository.findAll();
        entities.sort(new RepairDateComparator());
        Map<String, AtmRepairEntity> lastRepairDateMap = new HashMap<>();
        List<AtmRepair> recurringRepairs = new ArrayList<>();
        for (AtmRepairEntity entity : entities) {
            AtmRepairEntity entityFromMap = lastRepairDateMap.get(hashFrom(entity));
            if (entityFromMap != null && ChronoUnit.DAYS.between(entityFromMap.getEndDate(), entity.getStartDate()) <= 15) {
                AtmRepair repair = modelMapper.map(entityFromMap, AtmRepair.class);
                changeDateFormatForAtmRepair(repair);
                recurringRepairs.add(repair);
            }
            lastRepairDateMap.put(hashFrom(entity), entity);
        }
        return recurringRepairs;
    }

    private String hashFrom(AtmRepairEntity entity) {
        return entity.getAtmId() + "--" + entity.getReason();
    }

    private void changeDateFormatForAtmRepair(AtmRepair repair) {
        repair.setStartDate(parseDateToCorrectFormat(repair.getStartDate()));
        repair.setEndDate(parseDateToCorrectFormat(repair.getEndDate()));
    }

    private String parseDateToCorrectFormat(String date) {
        try {
            Date currentDate = FORMAT_FROM_DATABASE.parse(date);
            return TARGET_FORMAT.format(currentDate);
        } catch (ParseException e) {
            throw new CannotParseDateException(e.getMessage(), e);
        }
    }

    @Transactional
    private void saveDataToDatabase(List<AtmRepair> repairs) {
        for (AtmRepair repair : repairs) {
            AtmRepairEntity entity = modelMapper.map(repair, AtmRepairEntity.class);
            entity.setStartDate(parseStringToLocalDateTime(repair.getStartDate()));
            entity.setEndDate(parseStringToLocalDateTime(repair.getEndDate()));
            repository.save(entity);
        }
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

    private LocalDateTime parseStringToLocalDateTime(String date) {
        LocalDateTime parse;
        try {
            parse = LocalDateTime.parse(date, DateTimeFormatter.ofPattern(FORMAT_FROM_FILE));
        } catch (DateTimeParseException e) {
            throw new CannotParseDateException(e.getMessage(), e);
        }
        return parse;
    }
}