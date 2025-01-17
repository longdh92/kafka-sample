package org.example.fileproducer.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Value("${spring.kafka.topic.file-paths}")
    private String topicName;

    @Bean
    public NewTopic generateTopic() {
        return new NewTopic(this.topicName, 10, (short) 1);
    }
}
