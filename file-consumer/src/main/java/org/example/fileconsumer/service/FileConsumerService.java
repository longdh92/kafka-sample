package org.example.fileconsumer.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Log4j2
public class FileConsumerService {

    private final Queue<String> processingQueue = new LinkedList<>();

    private final AtomicInteger processedCount = new AtomicInteger(0);

    @KafkaListener(topics = "${spring.kafka.topic.file-paths}", groupId = "${spring.kafka.consumer.group-id}")
    public synchronized void consume(String filePath) {
        this.processingQueue.offer(filePath);
        this.processNextFile();
    }

    private void processNextFile() {
        if (!processingQueue.isEmpty()) {
            String nextFile = processingQueue.poll();
            this.processFile(nextFile, this::processNextFile);
        }
    }

    private void processFile(String filePath, Runnable callback) {
        log.info("Processing file: {}", filePath);
        try {
            // Giả lập thời gian xử lý
            Thread.sleep(100);

            processedCount.incrementAndGet();
            log.info("File processed successfully. Total processed count: {}", processedCount.get());
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        callback.run();
    }
}
