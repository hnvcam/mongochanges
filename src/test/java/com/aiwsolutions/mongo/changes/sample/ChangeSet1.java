package com.aiwsolutions.mongo.changes.sample;

import com.aiwsolutions.mongo.changes.ChangeSet;
import org.bson.Document;

/**
 * Created by camhoang on 11/22/15.
 */
public class ChangeSet1 extends ChangeSet {
    @Override
    public void run() {
        getMongoDatabase().getCollection("sample").insertOne(new Document("status", "failed"));
    }
}
