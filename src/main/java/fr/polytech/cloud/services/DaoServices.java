package fr.polytech.cloud.services;

import java.util.List;

public interface DaoServices<I> {

    public <T> T getOne(I id, Class<T> entityClass) throws Exception;

    public <T> List<T> getAll(Class<T> entityClass) throws Exception;

    public <T> List<T> getAll(String sortingCondition, Class<T> entityClass) throws Exception;

    public <T> List<T> getAllWithLimit(int initialOffset, int nbEntities, Class<T> entityClass) throws Exception;

    public <T> List<T> getAllWithLimit(int initialOffset, int nbEntities, String sortingCondition, Class<T> entityClass) throws Exception;

    public <T> List<T> getAllWhere(String query, Class<T> entityClass, Object... parameters) throws Exception;

    public <T> List<T> getAllWhere(String query, String sortingCondition, Class<T> entityClass, Object... parameters) throws Exception;

    public <T> List<T> getAllWhereWithLimit(String query, int initialOffset, int nbEntities, Class<T> entityClass, Object... parameters) throws Exception;

    public <T> List<T> getAllWhereWithLimit(String query, int initialOffset, int nbEntities, String sortingCondition, Class<T> entityClass, Object... parameters) throws Exception;

    public <T> int count(Class<T> entityClass) throws Exception;

    public <T> void insert(T object) throws Exception;

    public <T> void insertAll(T... objects) throws Exception;

    public <T> void update(I id, T object) throws Exception;

    public void delete(I id) throws Exception;

    public void deleteAll() throws Exception;
}