package fr.polytech.cloud.configurations;

public interface MongoDBConfiguration extends Configuration {

    public String getDatabaseName();

    public String getDatabaseCollection();
}