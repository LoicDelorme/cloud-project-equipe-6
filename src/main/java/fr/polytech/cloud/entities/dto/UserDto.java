package fr.polytech.cloud.entities.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.polytech.cloud.deserializers.UserDtoDeserializer;
import fr.polytech.cloud.entities.AbstractEntity;
import fr.polytech.cloud.serializers.BirthDayDtoSerializer;
import fr.polytech.cloud.serializers.PositionDtoSerializer;
import lombok.Data;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

@JsonDeserialize(using = UserDtoDeserializer.class)
public @Data class UserDto extends AbstractEntity {

    @MongoId
    @MongoObjectId
    private String id;

    private String lastName;

    private String firstName;

    @JsonSerialize(using = BirthDayDtoSerializer.class)
    private String birthDay;

    @JsonSerialize(using = PositionDtoSerializer.class)
    private PositionDto position;
}