package fr.polytech.cloud.serializers;

public interface Serializer<O> {

    public <I> O to(I in);
}