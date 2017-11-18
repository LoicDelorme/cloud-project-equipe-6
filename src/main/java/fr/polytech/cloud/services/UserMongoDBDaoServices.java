package fr.polytech.cloud.services;

import fr.polytech.cloud.collections.MongoCollectionManager;
import fr.polytech.cloud.configurations.MongoDBConfiguration;
import fr.polytech.cloud.entities.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserMongoDBDaoServices extends AbstractMongoDBDaoServices<UserDao> {

    @Autowired
    public UserMongoDBDaoServices(final MongoDBConfiguration mongoDBConfiguration) {
        super(UserDao.class, MongoCollectionManager.getMongoCollection(
                mongoDBConfiguration.getDatabaseHost(),
                mongoDBConfiguration.getDatabasePort(),
                mongoDBConfiguration.getDatabaseName(),
                mongoDBConfiguration.getDatabaseCollection()));
    }
}