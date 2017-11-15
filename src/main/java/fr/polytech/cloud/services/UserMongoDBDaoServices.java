package fr.polytech.cloud.services;

import fr.polytech.cloud.collections.MongoCollectionManager;
import fr.polytech.cloud.configurations.MongoDBConfiguration;
import fr.polytech.cloud.entities.UserMongoDBEntity;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMongoDBDaoServices extends AbstractMongoDBDaoServices<UserMongoDBEntity> {

    @Autowired
    public UserMongoDBDaoServices(final MongoDBConfiguration mongoDBConfiguration) {
        super(UserMongoDBEntity.class, MongoCollectionManager.getMongoCollection(
                mongoDBConfiguration.getDatabaseHost(),
                mongoDBConfiguration.getDatabasePort(),
                mongoDBConfiguration.getDatabaseName(),
                mongoDBConfiguration.getDatabaseCollection()));
    }
}