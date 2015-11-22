package com.aiwsolutions.mongo.changes;

import com.mongodb.Mongo;
import com.mongodb.client.MongoDatabase;

/**
 * Created by camhoang on 11/22/15.
 */
public abstract class ChangeSet {
    private MongoDatabase mongoDatabase;

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    public ChangeSet setMongoDatabase(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
        return this;
    }

    public abstract void run();
}
