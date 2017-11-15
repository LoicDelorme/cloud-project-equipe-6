package fr.polytech.cloud.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.polytech.cloud.controllers.UserController;
import fr.polytech.cloud.entities.PositionMongoDBEntity;
import fr.polytech.cloud.entities.UserMongoDBEntity;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class UserMongoDBEntitySerializer extends StdSerializer<UserMongoDBEntity> {

    public static final DateTimeFormatter DATE_PATTERN_IN = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter DATE_PATTERN_OUT = DateTimeFormatter.ofPattern("MM/dd/yyy");

    public UserMongoDBEntitySerializer() {
        this(null);
    }

    public UserMongoDBEntitySerializer(final Class<UserMongoDBEntity> t) {
        super(t);
    }

    @Override
    public void serialize(final UserMongoDBEntity userMongoDBEntity, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        final PositionMongoDBEntity position = userMongoDBEntity.getPosition();
        final List<Double> coordinates = position == null ? null : position.getCoordinates();
        final Double lon = coordinates == null ? null : coordinates.get(UserController.DEFAULT_COORDINATES_LON_OFFSET);
        final Double lat = coordinates == null ? null : coordinates.get(UserController.DEFAULT_COORDINATES_LAT_OFFSET);

        jsonGenerator.writeStartObject();

        jsonGenerator.writeObjectField("id", userMongoDBEntity.getId());
        jsonGenerator.writeObjectField("lastName", userMongoDBEntity.getLastName());
        jsonGenerator.writeObjectField("firstName", userMongoDBEntity.getFirstName());
        jsonGenerator.writeObjectField("birthDay", userMongoDBEntity.getBirthDay() == null ? null : LocalDate.parse(userMongoDBEntity.getBirthDay(), DATE_PATTERN_IN).format(DATE_PATTERN_OUT));

        jsonGenerator.writeObjectFieldStart("position");
        jsonGenerator.writeObjectField("lat", lat);
        jsonGenerator.writeObjectField("lon", lon);
        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
    }
}