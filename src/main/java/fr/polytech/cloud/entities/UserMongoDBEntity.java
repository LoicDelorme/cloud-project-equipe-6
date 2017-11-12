package fr.polytech.cloud.entities;

import lombok.Data;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.Date;

public @Data class UserMongoDBEntity extends AbstractMongoDBEntity {

    @MongoId
    @MongoObjectId
    private String id;

    private String lastName;

    private String firstName;

    private Date birthDay;

    private PositionMongoDBEntity position;
}