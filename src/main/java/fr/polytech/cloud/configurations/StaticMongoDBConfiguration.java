package fr.polytech.cloud.configurations;

public class StaticMongoDBConfiguration implements MongoDBConfiguration {

    private final String mongoDBHost;

    private final int mongoDBPort;

    private final String mongoDBDatabase;

    private final String mongoDBCollection;

    public StaticMongoDBConfiguration(final String mongoDBHost, final int mongoDBPort, final String mongoDBDatabase, final String mongoDBCollection) {
        this.mongoDBHost = mongoDBHost;
        this.mongoDBPort = mongoDBPort;
        this.mongoDBDatabase = mongoDBDatabase;
        this.mongoDBCollection = mongoDBCollection;
    }

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