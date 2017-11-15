package fr.polytech.cloud.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalSerializer extends StdSerializer<BigDecimal> {

    public BigDecimalSerializer() {
        this(null);
    }

    public BigDecimalSerializer(final Class<BigDecimal> t) {
        super(t);
    }

    @Override
    public void serialize(final BigDecimal bigDecimal, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(bigDecimal.toPlainString());
    }
}