package fr.polytech.cloud.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.polytech.cloud.entities.PositionMongoDBEntity;
import fr.polytech.cloud.entities.UserMongoDBEntity;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public class UserMongoDBEntityDeserializer extends StdDeserializer<UserMongoDBEntity> {

    public static final DateTimeFormatter DATE_PATTERN_IN = DateTimeFormatter.ofPattern("M/d/yyyy");

    public static final DateTimeFormatter DATE_PATTERN_OUT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public UserMongoDBEntityDeserializer() {
        this(null);
    }

    public UserMongoDBEntityDeserializer(final Class<UserMongoDBEntity> t) {
        super(t);
    }

    @Override
    public UserMongoDBEntity deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);

        final String id = rootNode.has("id") ? rootNode.get("id").asText() : null;
        final String lastName = rootNode.has("lastName") ? rootNode.get("lastName").asText() : null;
        final String firstName = rootNode.has("firstName") ? rootNode.get("firstName").asText() : null;
        final String birthDay = rootNode.has("birthDay") ? LocalDate.parse(rootNode.get("birthDay").asText(), DATE_PATTERN_IN).format(DATE_PATTERN_OUT) : null;

        final String type = "Point";
        BigDecimal lon = null;
        BigDecimal lat = null;
        if (rootNode.has("position")) {
            final JsonNode positionNode = rootNode.get("position");
            lon = positionNode.has("lon") ? new BigDecimal(positionNode.get("lon").asText()) : null;
            lat = positionNode.has("lat") ? new BigDecimal(positionNode.get("lat").asText()) : null;
        }
        final List<BigDecimal> coordinates = Arrays.asList(lon, lat);

        final PositionMongoDBEntity position = new PositionMongoDBEntity();
        position.setType(type);
        position.setCoordinates(coordinates);

        final UserMongoDBEntity user = new UserMongoDBEntity();
        user.setId(id);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setBirthDay(birthDay);
        user.setPosition(position);

        return user;
    }
}