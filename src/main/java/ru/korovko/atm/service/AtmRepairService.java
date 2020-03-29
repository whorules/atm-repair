package ru.korovko.atm.service;

import com.poiji.bind.Poiji;
import com.poiji.exception.PoijiExcelType;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.korovko.atm.dto.AtmRepair;
import ru.korovko.atm.entity.AtmRepairEntity;
import ru.korovko.atm.exception.CannotParseDateException;
import ru.korovko.atm.exception.IncorrectFileFormatException;
import ru.korovko.atm.repository.AtmRepairRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AtmRepairService {

    @Autowired
    private ModelMapper modelMapper;
    private final AtmRepairRepository repository;

    public Integer readFileFromExcel(MultipartFile file) throws IOException {
        List<AtmRepair> repairs = Poiji.fromExcel(file.getInputStream(), defineExcelType(file), AtmRepair.class);
        saveDataToDatabase(repairs);
        return repairs.size();
    }

    @Transactional
    public void deleteAll() {
        repository.deleteAll();
    }

    @Transactional
    public List<AtmRepair> getAllData() {
        List<AtmRepairEntity> entities = repository.findAll();
        return entities.stream()
                .map(entity -> modelMapper.map(entity, AtmRepair.class))
                .peek(this::changeDateForAtmRepair)
                .collect(Collectors.toList());
    }

    public void changeDateForAtmRepair(AtmRepair repair) {
        repair.setStartDate(parseDateToCorrectFormat(repair.getStartDate()));
        repair.setEndDate(parseDateToCorrectFormat(repair.getEndDate()));
    }

    public String parseDateToCorrectFormat(String date) {
        String currentPattern = "yyyy-MM-dd'T'HH:mm";
        String targetFormat = "dd-MM-yyyy HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(currentPattern);
        SimpleDateFormat targetSimpleDateFormat = new SimpleDateFormat(targetFormat);
        Date currentDate;
        try {
            currentDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new CannotParseDateException(e.getMessage());
        }
        return targetSimpleDateFormat.format(currentDate);
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

    private PoijiExcelType defineExcelType(MultipartFile file) {
        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xls")) {
            return PoijiExcelType.XLS;
        } else if (file.getOriginalFilename().endsWith(".xlsx")) {
            return PoijiExcelType.XLSX;
        } else throw new IncorrectFileFormatException("File format is incorrect");
    }

    private LocalDateTime parseStringToLocalDateTime(String date) {
        String pattern = "M/d/yy H:mm";
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }
}
