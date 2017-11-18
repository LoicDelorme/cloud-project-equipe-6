package fr.polytech.cloud.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import fr.polytech.cloud.entities.dto.PositionDto;
import fr.polytech.cloud.entities.dto.UserDto;

import java.io.IOException;

public class UserDtoDeserializer extends StdDeserializer<UserDto> {

    public UserDtoDeserializer() {
        this(null);
    }

    public UserDtoDeserializer(final Class<UserDto> t) {
        super(t);
    }

    @Override
    public UserDto deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);

        final String id = rootNode.has("id") ? rootNode.get("id").asText() : null;
        final String lastName = rootNode.has("lastName") ? rootNode.get("lastName").asText() : null;
        final String firstName = rootNode.has("firstName") ? rootNode.get("firstName").asText() : null;
        final String birthDay = rootNode.has("birthDay") ? rootNode.get("birthDay").asText() : null;
        final Double lon = rootNode.has("position") && rootNode.get("position").has("lon") ? Double.parseDouble(rootNode.get("position").get("lon").asText()) : null;
        final Double lat = rootNode.has("position") && rootNode.get("position").has("lat") ? Double.parseDouble(rootNode.get("position").get("lon").asText()) : null;

        final PositionDto position = new PositionDto();
        position.setLon(lon);
        position.setLat(lat);

        final UserDto user = new UserDto();
        user.setId(id);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setBirthDay(birthDay);
        user.setPosition(position);

        return user;
    }
}