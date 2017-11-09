package fr.polytech.cloud.services;

import fr.polytech.cloud.collections.MongoCollectionManager;
import fr.polytech.cloud.configurations.DynamicConfiguration;
import fr.polytech.cloud.entities.UserMongoDBEntity;

public class UserMongoDBDaoServices extends AbstractMongoDBDaoServices<UserMongoDBEntity> {

    public UserMongoDBDaoServices(DynamicConfiguration configuration) {
        super(UserMongoDBEntity.class,MongoCollectionManager.getMongoCollection(
                configuration.getProperty("spring.data.mongodb.host"),
                Integer.parseInt(configuration.getProperty("spring.data.mongodb.port")),
                configuration.getProperty("spring.data.mongodb.database"),
                configuration.getProperty("spring.data.mongodb.collection")
        ));
    }
}