package fr.polytech.cloud.collections;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MongoCollectionManager {

    public static final Logger LOGGER = LoggerFactory.getLogger(MongoCollectionManager.class);

    {
        final Properties properties = new Properties();
        try (InputStream inputStream = new FileInputStream("application.properties")) {
            properties.load(inputStream);

            databaseName = properties.getProperty("mongodb.database.name");
            collectionName = properties.getProperty("mongodb.collection.name");
        } catch (Exception ex) {
            LOGGER.error("Failed to load application.properties file!", ex);
        }
    }

    private static String databaseName;

    private static String collectionName;

    private static MongoCollection instance;

    private static final Object lock = new Object();

    private MongoCollectionManager() {
    }

    public static MongoCollection getMongoCollection() {
        synchronized (lock) {
            if (instance == null) {
                final DB database = new MongoClient().getDB(databaseName);
                instance = new Jongo(database).getCollection(collectionName);
            }

            return instance;
        }
    }
}