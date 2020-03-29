package ru.korovko.atm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.korovko.atm.dto.AtmRepair;
import ru.korovko.atm.service.AtmRepairService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/atmRepair")
@RequiredArgsConstructor
public class AtmRepairController {

    private final AtmRepairService service;

    @PostMapping()
    public Integer uploadFile(MultipartFile file) throws IOException {
        return service.readFileFromExcel(file);
    }

    @GetMapping()
    public List<AtmRepair> showAllData() {
        return service.getAllData();
    }

    @DeleteMapping()
    public void deleteAllData() {
        service.deleteAll();
    }

}
