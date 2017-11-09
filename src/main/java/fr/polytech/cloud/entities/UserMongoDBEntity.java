package fr.polytech.cloud.entities;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public class UserMongoDBEntity extends AbstractMongoDBEntity {

    @MongoId
    @MongoObjectId
    private String id;

    private String lastName;

    private String firstName;

    private String birthDay;

    private PositionMongoDBEntity position;

    @Override
    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(final String birthDay) {
        this.birthDay = birthDay;
    }

    public PositionMongoDBEntity getPosition() {
        return position;
    }

    public void setPosition(final PositionMongoDBEntity position) {
        this.position = position;
    }
}