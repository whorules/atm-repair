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
@CrossOrigin("*")
public class AtmRepairController {

    private final AtmRepairService service;

    @PostMapping()
    public AtmRepair uploadFile(MultipartFile file) throws IOException {
        return service.uploadExcelFileToDatabase(file);
    }

    @GetMapping("/getAll")
    public List<AtmRepair> getAllAtmRepairEvents() {
        return service.getAllAtmRepairs();
    }

    @DeleteMapping()
    public void deleteAllData() {
        service.deleteAll();
    }

    @GetMapping("/reasons")
    public List<String> getTopRepairReasons(@RequestParam Integer count) {
        return service.getTopRecurringReasons(count);
    }

    @GetMapping("/longest")
    public List<AtmRepair> getTopLongestRepairs(@RequestParam Integer count) {
        return service.getTopLongestRepairs(count);
    }

    @GetMapping("/recurring")
    public List<AtmRepair> getRecurringRepairs() {
        return service.getRecurringRepairs();
    }
}
