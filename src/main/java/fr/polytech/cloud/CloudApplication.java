package fr.polytech.cloud;

import fr.polytech.cloud.configurations.DynamicMongoDBConfiguration;
import fr.polytech.cloud.configurations.MongoDBConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }

    @Bean
    public MongoDBConfiguration mongoDBConfiguration() {
        return new DynamicMongoDBConfiguration();
    }
}