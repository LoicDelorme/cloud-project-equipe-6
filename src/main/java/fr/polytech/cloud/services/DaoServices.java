package fr.polytech.cloud.services;

import java.util.List;

public interface DaoServices<T, I> {

    public T getOne(I id) throws Exception;

    public List<T> getAll() throws Exception;

    public List<T> getAllWithLimit(int initialOffset, int nbEntities) throws Exception;

    public List<T> getAllWhere(I parameters) throws Exception;

    public List<T> getAllWhereWithLimit(I parameters, int initialOffset, int nbEntities) throws Exception;

    public int count() throws Exception;

    public void insert(T object) throws Exception;

    public void insertAll(T... objects) throws Exception;

    public void update(I id, T object) throws Exception;

    public void delete(I id) throws Exception;

    public void deleteAll() throws Exception;
}