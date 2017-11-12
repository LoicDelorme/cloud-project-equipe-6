package fr.polytech.cloud.deserializers;

public interface Deserializer {

    public <O> O from(String in, Class<O> clazz) throws Exception;
}