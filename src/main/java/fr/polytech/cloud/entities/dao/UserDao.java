package fr.polytech.cloud.entities.dao;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.polytech.cloud.deserializers.BirthDayDaoDeserializer;
import fr.polytech.cloud.entities.AbstractEntity;
import fr.polytech.cloud.serializers.UserDaoSerializer;
import lombok.Data;

@JsonSerialize(using = UserDaoSerializer.class)
public @Data class UserDao extends AbstractEntity {

    private String id;

    private String lastName;

    private String firstName;

    @JsonDeserialize(using = BirthDayDaoDeserializer.class)
    private String birthDay;

    private PositionDao position;
}