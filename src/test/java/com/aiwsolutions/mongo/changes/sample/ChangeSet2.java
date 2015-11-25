package com.aiwsolutions.mongo.changes.sample;

import com.aiwsolutions.mongo.changes.ChangeSet;
import org.bson.Document;

/**
 * Created by camhoang on 11/22/15.
 */
public class ChangeSet2 extends ChangeSet {
    public static final String CHANGE_SET_COLLECTION = "sample";
    public static final String DOCUMENT_KEY = "status";
    public static final String DOCUMENT_VALUE = "ok";

    @Override
    public void run() {
        getMongoDatabase().getCollection(CHANGE_SET_COLLECTION).insertOne(new Document(DOCUMENT_KEY, DOCUMENT_VALUE));
    }
}
