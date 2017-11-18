package fr.polytech.cloud.services;

import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.util.ArrayList;
import java.util.List;

public class AbstractMongoDBDaoServices implements DaoServices<String> {

    private final MongoCollection mongoCollection;

    public AbstractMongoDBDaoServices(MongoCollection mongoCollection) {
        this.mongoCollection = mongoCollection;
    }

    @Override
    public <T> T getOne(final String id, final Class<T> entityClass) throws Exception {
        return this.mongoCollection.findOne(new ObjectId(id)).as(entityClass);
    }

    @Override
    public <T> List<T> getAll(final Class<T> entityClass) throws Exception {
        final List<T> entities = new ArrayList<T>();

        final MongoCursor<T> mongoCursor = this.mongoCollection.find().as(entityClass);
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public <T> List<T> getAll(final String sortingCondition, final Class<T> entityClass) throws Exception {
        final List<T> entities = new ArrayList<T>();

        final MongoCursor<T> mongoCursor = this.mongoCollection.find().sort(sortingCondition).as(entityClass);
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public <T> List<T> getAllWithLimit(final int initialOffset, final int nbEntities, final Class<T> entityClass) throws Exception {
        final List<T> entities = new ArrayList<T>();

        final MongoCursor<T> mongoCursor = this.mongoCollection.find().skip(initialOffset).limit(nbEntities).as(entityClass);
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public <T> List<T> getAllWithLimit(final int initialOffset, final int nbEntities, final String sortingCondition, final Class<T> entityClass) throws Exception {
        final List<T> entities = new ArrayList<T>();

        final MongoCursor<T> mongoCursor = this.mongoCollection.find().skip(initialOffset).limit(nbEntities).sort(sortingCondition).as(entityClass);
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public <T> List<T> getAllWhere(final String query, final Class<T> entityClass, final Object... parameters) throws Exception {
        final MongoCursor<T> mongoCursor = this.mongoCollection.find(query, parameters).as(entityClass);

        final List<T> entities = new ArrayList<T>();
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public <T> List<T> getAllWhere(final String query, final String sortingCondition, final Class<T> entityClass, final Object... parameters) throws Exception {
        final MongoCursor<T> mongoCursor = this.mongoCollection.find(query, parameters).sort(sortingCondition).as(entityClass);

        final List<T> entities = new ArrayList<T>();
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public <T> List<T> getAllWhereWithLimit(final String query, final int initialOffset, final int nbEntities, final Class<T> entityClass, final Object... parameters) throws Exception {
        final MongoCursor<T> mongoCursor = this.mongoCollection.find(query, parameters).skip(initialOffset).limit(nbEntities).as(entityClass);

        final List<T> entities = new ArrayList<T>();
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public <T> List<T> getAllWhereWithLimit(final String query, final int initialOffset, final int nbEntities, final String sortingCondition, final Class<T> entityClass, final Object... parameters) throws Exception {
        final MongoCursor<T> mongoCursor = this.mongoCollection.find(query, parameters).sort(sortingCondition).skip(initialOffset).limit(nbEntities).as(entityClass);

        final List<T> entities = new ArrayList<T>();
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public <T> int count(final Class<T> entityClass) throws Exception {
        final MongoCursor<T> mongoCursor = this.mongoCollection.find().as(entityClass);
        final int nbEntities = mongoCursor.count();
        mongoCursor.close();

        return nbEntities;
    }

    @Override
    public <T> void insert(final T object) throws Exception {
        this.mongoCollection.insert(object);
    }

    @Override
    public <T> void insertAll(final T... objects) throws Exception {
        this.mongoCollection.insert(objects);
    }

    @Override
    public <T> void update(final String id, final T object) throws Exception {
        this.mongoCollection.update(new ObjectId(id)).with(object);
    }

    @Override
    public void delete(final String id) throws Exception {
        this.mongoCollection.remove(new ObjectId(id));
    }

    @Override
    public void deleteAll() throws Exception {
        this.mongoCollection.remove();
    }
}