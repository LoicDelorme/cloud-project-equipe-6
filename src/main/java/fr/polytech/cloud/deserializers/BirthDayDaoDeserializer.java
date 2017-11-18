package fr.polytech.cloud.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BirthDayDaoDeserializer extends StdDeserializer<String> {

    public static final DateTimeFormatter DATE_PATTERN_IN = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static final DateTimeFormatter DATE_PATTERN_OUT = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public BirthDayDaoDeserializer() {
        this(null);
    }

    public BirthDayDaoDeserializer(final Class<String> t) {
        super(t);
    }

    @Override
    public String deserialize(final JsonParser jsonParser, final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonNode rootNode = jsonParser.getCodec().readTree(jsonParser);

        if (rootNode.has("birthDay")) {
            return LocalDate.parse(rootNode.get("birthDay").asText(), DATE_PATTERN_IN).format(DATE_PATTERN_OUT);
        }

        return null;
    }
}