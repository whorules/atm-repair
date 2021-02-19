package ru.korovko.atm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.korovko.atm.dto.AtmRepairResponse;
import ru.korovko.atm.dto.UploadedFilesResponse;
import ru.korovko.atm.service.AtmRepairService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/atm-repair")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AtmRepairController {

    private final AtmRepairService service;

    @PostMapping
    public UploadedFilesResponse upload(MultipartFile file) throws IOException {
        return service.upload(file);
    }

    @GetMapping
    public List<AtmRepairResponse> getAllAtmRepairEvents() {
        return service.getAll();
    }

    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }

    @GetMapping("/reasons")
    public List<String> getTopRepairReasons(@RequestParam Integer count) {
        return service.getTopRecurringReasons(count);
    }

    @GetMapping("/longest")
    public List<AtmRepairResponse> getTopLongestRepairs(@RequestParam Integer count) {
        return service.getTopLongestRepairs(count);
    }

    @GetMapping("/recurring")
    public List<AtmRepairResponse> getRecurringRepairs() {
        return service.getRecurringRepairs();
    }
}
