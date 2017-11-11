package fr.polytech.cloud.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:application.properties")
public class DynamicMongoDBConfiguration implements MongoDBConfiguration {

    @Value("${spring.data.mongodb.host}")
    private String mongoDBHost;

    @Value("${spring.data.mongodb.port}")
    private int mongoDBPort;

    @Value("${spring.data.mongodb.database}")
    private String mongoDBDatabase;

    @Value("${spring.data.mongodb.collection}")
    private String mongoDBCollection;

    @Override
    public String getDatabaseName() {
        return this.mongoDBDatabase;
    }

    @Override
    public String getDatabaseCollection() {
        return this.mongoDBCollection;
    }

    @Override
    public String getDatabaseHost() {
        return this.mongoDBHost;
    }

    @Override
    public int getDatabasePort() {
        return this.mongoDBPort;
    }
}