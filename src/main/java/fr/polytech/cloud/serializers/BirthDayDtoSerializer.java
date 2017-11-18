package fr.polytech.cloud.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BirthDayDtoSerializer extends JsonSerializer<String> {

    public static final DateTimeFormatter DATE_PATTERN_IN = DateTimeFormatter.ofPattern("M/d/yyyy");

    public static final DateTimeFormatter DATE_PATTERN_OUT = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Override
    public void serialize(final String string, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(LocalDate.parse(string, DATE_PATTERN_IN).format(DATE_PATTERN_OUT));
    }
}