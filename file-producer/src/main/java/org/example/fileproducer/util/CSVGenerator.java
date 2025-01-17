package org.example.fileproducer.util;

import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.UUID;

@Log4j2
public class CSVGenerator {

    private static final String directoryPath = "data-csv";

    private static final int numberOfFiles = 1000;

    public static void main(String[] args) {
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                log.info("Created directory: {}", directoryPath);
            } else {
                log.error("Failed to create directory: {}", directoryPath);
                return;
            }
        }

        for (int i = 1; i <= numberOfFiles; i++) {
            String fileName = "file_" + UUID.randomUUID() + ".csv";
            File csvFile = new File(directory, fileName);

            try (FileWriter writer = new FileWriter(csvFile)) {
                log.info("Created file: {}", csvFile.getAbsolutePath());
            } catch (IOException e) {
                log.error("Failed to create file: {}", csvFile.getAbsolutePath());
                log.error(e.getMessage());
            }
        }

        log.info("Finished creating " + numberOfFiles + " files in directory: " + directoryPath);
    }
}
