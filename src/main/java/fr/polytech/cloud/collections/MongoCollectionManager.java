package fr.polytech.cloud.collections;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import org.jongo.Jongo;
import org.jongo.MongoCollection;

import java.util.HashMap;
import java.util.Map;

public class MongoCollectionManager {

    private static final Map<String, MongoCollection> mongoCollections = new HashMap<String, MongoCollection>();

    private static final Object lock = new Object();

    private MongoCollectionManager() {
    }

    public static MongoCollection getMongoCollection(String hostname, int port, String databaseName, String collectionName) {
        synchronized (lock) {
            final String key = databaseName + "-" + collectionName;
            if (!mongoCollections.containsKey(key)) {
                final DB database = new MongoClient(hostname, port).getDB(databaseName);
                final Jongo jongo = new Jongo(database);

                mongoCollections.put(key, jongo.getCollection(collectionName));
            }

            return mongoCollections.get(key);
        }
    }
}