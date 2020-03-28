package ru.korovko.atm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.korovko.atm.service.AtmRepairService;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class AtmRepairController {

    private final AtmRepairService service;

    @PostMapping("/upload")
    public String uploadFile(MultipartFile file) throws IOException {
        service.readFileFromExcel(file);
        return null;
    }
}
