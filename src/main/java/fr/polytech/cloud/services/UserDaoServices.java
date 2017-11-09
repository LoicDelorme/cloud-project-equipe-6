package fr.polytech.cloud.services;

import fr.polytech.cloud.collections.MongoCollectionManager;
import fr.polytech.cloud.entities.User;

public class UserDaoServices extends AbstractMongoDBDaoServices<User> {

    private static final String databaseName = "TP_CLOUD";

    private static final String collectionName = "users";

    public UserDaoServices() {
        super(User.class, MongoCollectionManager.getMongoCollection(databaseName, collectionName));
    }
}