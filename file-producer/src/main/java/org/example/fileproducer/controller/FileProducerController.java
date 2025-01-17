package org.example.fileproducer.controller;

import lombok.RequiredArgsConstructor;
import org.example.fileproducer.service.FileProducerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file-producer")
@RequiredArgsConstructor
public class FileProducerController {

    private final FileProducerService fileProducerService;

    @PostMapping("/send")
    public ResponseEntity<String> sendFiles(@RequestParam String directoryPath) {
        try {
            this.fileProducerService.sendFilePaths(directoryPath);
            return ResponseEntity.ok("File paths sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
