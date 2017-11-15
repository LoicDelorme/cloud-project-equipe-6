package fr.polytech.cloud.services;

import fr.polytech.cloud.entities.AbstractMongoDBEntity;
import org.bson.types.ObjectId;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.util.ArrayList;
import java.util.List;

public class AbstractMongoDBDaoServices<T extends AbstractMongoDBEntity> implements DaoServices<T, String> {

    private final Class<T> entityClass;

    private final MongoCollection mongoCollection;

    public AbstractMongoDBDaoServices(Class<T> entityClass, MongoCollection mongoCollection) {
        this.entityClass = entityClass;
        this.mongoCollection = mongoCollection;
    }

    @Override
    public T getOne(final String id) throws Exception {
        return this.mongoCollection.findOne(new ObjectId(id)).as(this.entityClass);
    }

    @Override
    public List<T> getAll() throws Exception {
        final List<T> entities = new ArrayList<T>();

        final MongoCursor<T> mongoCursor = this.mongoCollection.find().as(this.entityClass);
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public List<T> getAll(final String sortingCondition) throws Exception {
        final List<T> entities = new ArrayList<T>();

        final MongoCursor<T> mongoCursor = this.mongoCollection.find().sort(sortingCondition).as(this.entityClass);
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public List<T> getAllWithLimit(final int initialOffset, final int nbEntities) throws Exception {
        final List<T> entities = new ArrayList<T>();

        final MongoCursor<T> mongoCursor = this.mongoCollection.find().skip(initialOffset).limit(nbEntities).as(this.entityClass);
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public List<T> getAllWithLimit(final int initialOffset, final int nbEntities, final String sortingCondition) throws Exception {
        final List<T> entities = new ArrayList<T>();

        final MongoCursor<T> mongoCursor = this.mongoCollection.find().skip(initialOffset).limit(nbEntities).sort(sortingCondition).as(this.entityClass);
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public List<T> getAllWhere(final String query, Object... parameters) throws Exception {
        final MongoCursor<T> mongoCursor = this.mongoCollection.find(query, parameters).as(this.entityClass);

        final List<T> entities = new ArrayList<T>();
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public List<T> getAllWhere(final String query, final String sortingCondition, final Object... parameters) throws Exception {
        final MongoCursor<T> mongoCursor = this.mongoCollection.find(query, parameters).sort(sortingCondition).as(this.entityClass);

        final List<T> entities = new ArrayList<T>();
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public List<T> getAllWhereWithLimit(final String query, final int initialOffset, final int nbEntities, Object... parameters) throws Exception {
        final MongoCursor<T> mongoCursor = this.mongoCollection.find(query, parameters).skip(initialOffset).limit(nbEntities).as(this.entityClass);

        final List<T> entities = new ArrayList<T>();
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public List<T> getAllWhereWithLimit(final String query, final int initialOffset, final int nbEntities, final String sortingCondition, final Object... parameters) throws Exception {
        final MongoCursor<T> mongoCursor = this.mongoCollection.find(query, parameters).sort(sortingCondition).skip(initialOffset).limit(nbEntities).as(this.entityClass);

        final List<T> entities = new ArrayList<T>();
        mongoCursor.forEach(entity -> entities.add(entity));
        mongoCursor.close();

        return entities;
    }

    @Override
    public int count() throws Exception {
        final MongoCursor<T> mongoCursor = this.mongoCollection.find().as(this.entityClass);
        final int nbEntities = mongoCursor.count();
        mongoCursor.close();

        return nbEntities;
    }

    @Override
    public void insert(final T object) throws Exception {
        this.mongoCollection.insert(object);
    }

    @Override
    public void insertAll(final T... objects) throws Exception {
        this.mongoCollection.insert(objects);
    }

    @Override
    public void update(final String id, final T object) throws Exception {
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