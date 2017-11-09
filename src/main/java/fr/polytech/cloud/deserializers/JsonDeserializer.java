package fr.polytech.cloud.deserializers;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

public class JsonDeserializer implements Deserializer<String> {

    private static final Jsonb jsonBuilder = JsonbBuilder.create();

    @Override
    public <O> O from(String in, Class<O> clazz) {
        return jsonBuilder.fromJson(in, clazz);
    }
}