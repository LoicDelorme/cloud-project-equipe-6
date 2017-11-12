package fr.polytech.cloud.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.polytech.cloud.controllers.UserController;
import fr.polytech.cloud.entities.PositionMongoDBEntity;
import fr.polytech.cloud.entities.UserMongoDBEntity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class UserMongoDBEntitySerializer extends StdSerializer<UserMongoDBEntity> {

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("MM/dd/yyyy");

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
        final Double lon = (coordinates == null || coordinates.isEmpty()) ? null : coordinates.get(UserController.DEFAULT_COORDINATES_LON_OFFSET);
        final Double lat = (coordinates == null || coordinates.size() != UserController.DEFAULT_COORDINATES_SIZE) ? null : coordinates.get(UserController.DEFAULT_COORDINATES_LAT_OFFSET);

        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", userMongoDBEntity.getId());
        jsonGenerator.writeStringField("lastName", userMongoDBEntity.getLastName());
        jsonGenerator.writeStringField("firstName", userMongoDBEntity.getFirstName());
        jsonGenerator.writeStringField("birthDay", userMongoDBEntity.getBirthDay() == null ? null : DATE_FORMATTER.format(userMongoDBEntity.getBirthDay()));

        jsonGenerator.writeObjectFieldStart("position");
        jsonGenerator.writeObjectField("lat", lat);
        jsonGenerator.writeObjectField("lon", lon);
        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
    }
}