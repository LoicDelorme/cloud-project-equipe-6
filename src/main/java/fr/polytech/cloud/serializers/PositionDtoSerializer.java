package fr.polytech.cloud.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import fr.polytech.cloud.controllers.UserController;
import fr.polytech.cloud.entities.dto.PositionDto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PositionDtoSerializer extends StdSerializer<PositionDto> {

    public PositionDtoSerializer() {
        this(null);
    }

    public PositionDtoSerializer(final Class<PositionDto> t) {
        super(t);
    }

    @Override
    public void serialize(final PositionDto positionDto, final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException {
        final String type = "Point";

        final List<Double> coordinates = new ArrayList<Double>();
        coordinates.add(UserController.DEFAULT_COORDINATES_LON_OFFSET, positionDto.getLon());
        coordinates.add(UserController.DEFAULT_COORDINATES_LAT_OFFSET, positionDto.getLat());

        jsonGenerator.writeObjectField("type", type);
        jsonGenerator.writeObjectField("coordinates", coordinates);
    }
}