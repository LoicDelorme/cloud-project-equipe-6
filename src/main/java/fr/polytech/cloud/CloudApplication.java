package fr.polytech.cloud;

import fr.polytech.cloud.configurations.DynamicConfiguration;
import fr.polytech.cloud.services.UserMongoDBDaoServices;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CloudApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudApplication.class, args);
    }

    @Bean
    public DynamicConfiguration dynamicConfiguration() {
        return new DynamicConfiguration();
    }

    @Bean
    public UserMongoDBDaoServices userMongoDBDaoServices() {
        return new UserMongoDBDaoServices(dynamicConfiguration());
    }
}