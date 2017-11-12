package fr.polytech.cloud.controllers;

import fr.polytech.cloud.deserializers.Deserializer;
import fr.polytech.cloud.deserializers.JsonDeserializer;
import fr.polytech.cloud.serializers.JsonSerializer;
import fr.polytech.cloud.serializers.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractController {

    public static final Logger LOGGER = LoggerFactory.getLogger(AbstractController.class);

    public static final Serializer SERIALIZER = new JsonSerializer();

    public static final Deserializer DESERIALIZER = new JsonDeserializer();
}