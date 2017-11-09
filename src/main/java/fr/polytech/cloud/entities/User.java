package fr.polytech.cloud.entities;

import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.Date;

public class User extends AbstractMongoDBEntity {

    @MongoId
    @MongoObjectId
    private String id;

    private String lastName;

    private String firstName;

    private Date birthDay;

    private Position position;

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

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(final Date birthDay) {
        this.birthDay = birthDay;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }
}