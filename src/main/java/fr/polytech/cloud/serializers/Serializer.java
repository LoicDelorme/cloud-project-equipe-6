package fr.polytech.cloud.serializers;

public interface Serializer {

    public <I> String to(I in) throws Exception;
}