package com.aiwsolutions.mongo.changes;

import com.mongodb.Mongo;

/**
 * Created by camhoang on 11/22/15.
 */
public abstract class ChangeSet {
    private Mongo mongo;

    public Mongo getMongo() {
        return mongo;
    }

    public ChangeSet setMongo(Mongo mongo) {
        this.mongo = mongo;
        return this;
    }

    public abstract void execute();
}
