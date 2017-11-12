package fr.polytech.cloud.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.polytech.cloud.forms.UserForm;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class UserFormSerializer extends StdSerializer<UserForm> {

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("dd/MM/yyyy");

    public UserFormSerializer() {
        this(null);
    }

    public UserFormSerializer(final Class<UserForm> t) {
        super(t);
    }

    @Override
    public void serialize(final UserForm userForm, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeStringField("id", userForm.getId());
        jsonGenerator.writeStringField("lastName", userForm.getLastName());
        jsonGenerator.writeStringField("firstName", userForm.getFirstName());
        jsonGenerator.writeStringField("birthDay", userForm.getBirthDay() == null ? null : DATE_FORMATTER.format(userForm.getBirthDay()));

        jsonGenerator.writeObjectFieldStart("position");
        jsonGenerator.writeObjectField("lat", userForm.getLat());
        jsonGenerator.writeObjectField("lon", userForm.getLon());
        jsonGenerator.writeEndObject();

        jsonGenerator.writeEndObject();
    }
}