package fr.polytech.cloud.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.DoubleNode;
import fr.polytech.cloud.entities.PositionMongoDBEntity;
import fr.polytech.cloud.entities.UserMongoDBEntity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class UserMongoDBEntityDeserializer extends StdDeserializer<UserMongoDBEntity> {

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    public UserMongoDBEntityDeserializer() {
        this(null);
    }

    public UserMongoDBEntityDeserializer(final Class<UserMongoDBEntity> t) {
        super(t);
    }

    @Override
    public UserMongoDBEntity deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        final String id = node.has("id") ? node.get("id").asText() : null;
        final String lastName = node.has("lastName") ? node.get("lastName").asText() : null;
        final String firstName = node.has("firstName") ? node.get("firstName").asText() : null;
        Date birthDay;
        try {
            birthDay = DATE_FORMATTER.parse(node.get("birthDay").asText());
        } catch (Exception e) {
            birthDay = null;
        }
        Double lat = null;
        Double lon = null;
        if (node.has("position")) {
            final JsonNode positionNode = node.get("position");
            lat = positionNode.has("lat") ? ((DoubleNode) positionNode.get("lat")).doubleValue() : null;
            lon = positionNode.has("lon") ? ((DoubleNode) positionNode.get("lon")).doubleValue() : null;
        }

        final PositionMongoDBEntity position = new PositionMongoDBEntity();
        position.setType("Point");
        position.setCoordinates(node.has("position") ? Arrays.asList(lat, lon) : null);

        final UserMongoDBEntity user = new UserMongoDBEntity();
        user.setId(id);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setBirthDay(birthDay);
        user.setPosition(position);

        return user;
    }
}