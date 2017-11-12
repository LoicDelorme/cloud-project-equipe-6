package fr.polytech.cloud.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.polytech.cloud.entities.UserMongoDBEntity;
import fr.polytech.cloud.forms.UserForm;

public class JsonSerializer implements Serializer {

    private final ObjectMapper mapper;

    public JsonSerializer() {
        final SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(UserMongoDBEntity.class, new UserMongoDBEntitySerializer());
        simpleModule.addSerializer(UserForm.class, new UserFormSerializer());

        this.mapper = new ObjectMapper();
        this.mapper.registerModule(simpleModule);
    }

    @Override
    public <I> String to(I in) throws Exception {
        return this.mapper.writeValueAsString(in);
    }
}