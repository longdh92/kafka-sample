package org.example.fileproducer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

@Service
@RequiredArgsConstructor
@Log4j2
public class FileProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${spring.kafka.topic.file-paths}")
    private String topicName;

    public void sendFilePaths(String directoryPath) {
        File folder = new File(directoryPath);
        if (!folder.exists() || !folder.isDirectory()) {
            throw new IllegalArgumentException("The directory path is invalid or not accessible: " + directoryPath);
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            Arrays.sort(files, Comparator.comparingLong(File::lastModified));
            log.info("Found {} CSV files in directory: {}", files.length, directoryPath);
            for (File file : files) {
                this.kafkaTemplate.send(new ProducerRecord<>(this.topicName, file.getName(), file.getAbsolutePath()))
                        .thenAccept(result -> log.info(
                                "Message [{}] sent successfully to partition [{}], offset [{}]",
                                file.getAbsolutePath(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset()
                        ))
                        .exceptionally(ex -> {
                            log.error("Failed to send file path: {} due to {}", file.getAbsolutePath(), ex.getMessage());
                            return null;
                        });
            }
        } else {
            log.info("No CSV files found in directory: {}", directoryPath);
        }
    }
}
