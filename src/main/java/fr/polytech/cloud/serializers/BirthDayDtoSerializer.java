package fr.polytech.cloud.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BirthDayDtoSerializer extends StdSerializer<String> {

    public static final DateTimeFormatter DATE_PATTERN_IN = DateTimeFormatter.ofPattern("M/d/yyyy");

    public static final DateTimeFormatter DATE_PATTERN_OUT = DateTimeFormatter.ofPattern("yyyyMMdd");

    public BirthDayDtoSerializer() {
        this(null);
    }

    public BirthDayDtoSerializer(final Class<String> t) {
        super(t);
    }

    @Override
    public void serialize(final String string, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(LocalDate.parse(string, DATE_PATTERN_IN).format(DATE_PATTERN_OUT));
    }
}