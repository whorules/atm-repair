package ru.korovko.atm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.korovko.atm.dto.AtmRepair;
import ru.korovko.atm.service.AtmRepairService;
import ru.korovko.atm.service.handler.AtmRepairHandler;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/atmRepair")
@RequiredArgsConstructor
public class AtmRepairController {

    private final AtmRepairService service;
    private final AtmRepairHandler handler;

    @PostMapping()
    public Integer uploadFile(MultipartFile file) throws IOException {
        return service.uploadExcelFileToDatabase(file);
    }

    @GetMapping("/getAll")
    public List<AtmRepair> showAllData() {
        return service.getAllData();
    }

    @DeleteMapping()
    public void deleteAllData() {
        service.deleteAll();
    }

    @GetMapping("/reasons")
    public List<String> getTopThreeRepairReasons() {
        return handler.getTopThreeRecurringReasons();
    }

    @GetMapping("/longest")
    public List<AtmRepair> getTopThreeLongestRepairs() {
        return handler.getTopThreeLongestRepairs();
    }

    @GetMapping("/recurring")
    public List<AtmRepair> getRecurringRepairs() {
        return handler.getAllRecurringRepairs();
    }
}
