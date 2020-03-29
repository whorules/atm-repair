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
import ru.korovko.atm.exception.IncorrectFileFormatException;
import ru.korovko.atm.repository.AtmRepairRepository;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AtmRepairService {

    private final AtmRepairRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public void readFileFromExcel(MultipartFile file) throws IOException {
        List<AtmRepair> repairs = Poiji.fromExcel(file.getInputStream(), defineExcelType(file), AtmRepair.class);
        saveDataToDatabase(repairs);
    }

    private PoijiExcelType defineExcelType(MultipartFile file) {
        if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xls")) {
            return PoijiExcelType.XLS;
        } else if (file.getOriginalFilename().endsWith(".xlsx")) {
            return PoijiExcelType.XLSX;
        } else throw new IncorrectFileFormatException("File format is incorrect");
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

    private LocalDateTime parseStringToLocalDateTime(String date) {
        String pattern = "M/d/yy H:mm";
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(pattern));
    }
}
