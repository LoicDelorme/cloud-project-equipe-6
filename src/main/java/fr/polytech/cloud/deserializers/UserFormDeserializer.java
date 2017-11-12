package fr.polytech.cloud.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.DoubleNode;
import fr.polytech.cloud.forms.UserForm;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserFormDeserializer extends StdDeserializer<UserForm> {

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    public UserFormDeserializer() {
        this(null);
    }

    public UserFormDeserializer(final Class<UserForm> t) {
        super(t);
    }

    @Override
    public UserForm deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
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

        final UserForm userForm = new UserForm();
        userForm.setId(id);
        userForm.setLastName(lastName);
        userForm.setFirstName(firstName);
        userForm.setBirthDay(birthDay);
        userForm.setLat(lat);
        userForm.setLon(lon);

        return userForm;
    }
}