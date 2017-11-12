package fr.polytech.cloud.deserializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import fr.polytech.cloud.entities.UserMongoDBEntity;
import fr.polytech.cloud.forms.UserForm;

public class JsonDeserializer implements Deserializer {

    private final ObjectMapper mapper;

    public JsonDeserializer() {
        final SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(UserMongoDBEntity.class, new UserMongoDBEntityDeserializer());
        simpleModule.addDeserializer(UserForm.class, new UserFormDeserializer());

        this.mapper = new ObjectMapper();
        this.mapper.registerModule(simpleModule);
    }

    @Override
    public <O> O from(String in, Class<O> clazz) throws Exception {
        return this.mapper.readValue(in, clazz);
    }
}