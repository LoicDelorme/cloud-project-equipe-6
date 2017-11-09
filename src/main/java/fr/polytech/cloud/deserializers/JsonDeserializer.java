package fr.polytech.cloud.deserializers;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;
import java.lang.reflect.Type;

public class JsonDeserializer implements Deserializer<String> {

    private static final Jsonb jsonBuilder = JsonbBuilder.create(new JsonbConfig());

    @Override
    public <O> O from(String in, Class<O> clazz) {
        return jsonBuilder.fromJson(in, clazz);
    }

    @Override
    public <O> O from(final String in, final Type type) {
        return jsonBuilder.fromJson(in, type);
    }
}