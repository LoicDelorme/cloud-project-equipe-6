package fr.polytech.cloud.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.polytech.cloud.controllers.UserController;
import fr.polytech.cloud.entities.dao.PositionDao;
import fr.polytech.cloud.entities.dao.UserDao;

import java.io.IOException;
import java.util.List;

public class UserDaoSerializer extends StdSerializer<UserDao> {

    public UserDaoSerializer() {
        this(null);
    }

    public UserDaoSerializer(final Class<UserDao> t) {
        super(t);
    }

    @Override
    public void serialize(final UserDao userDao, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        final PositionDao position = userDao.getPosition();
        final List<Double> coordinates = position == null ? null : position.getCoordinates();
        final Double lon = coordinates == null ? null : coordinates.get(UserController.DEFAULT_COORDINATES_LON_OFFSET);
        final Double lat = coordinates == null ? null : coordinates.get(UserController.DEFAULT_COORDINATES_LAT_OFFSET);

        jsonGenerator.writeStartObject();

        jsonGenerator.writeObjectField("id", userDao.getId());
        jsonGenerator.writeObjectField("lastName", userDao.getLastName());
        jsonGenerator.writeObjectField("firstName", userDao.getFirstName());
        jsonGenerator.writeObjectField("birthDay", userDao.getBirthDay());

        jsonGenerator.writeObjectFieldStart("position");
        jsonGenerator.writeObjectField("lat", lat);
        jsonGenerator.writeObjectField("lon", lon);
        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
    }
}