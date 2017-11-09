package fr.polytech.cloud.serializers;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.json.bind.JsonbConfig;

public class JsonSerializer implements Serializer<String> {

    private static final Jsonb jsonBuilder = JsonbBuilder.create(new JsonbConfig().withNullValues(true));

    @Override
    public <I> String to(I in) {
        return jsonBuilder.toJson(in);
    }
}