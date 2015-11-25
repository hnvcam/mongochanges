package com.aiwsolutions.mongo.changes.runner;

import com.aiwsolutions.mongo.changes.ChangeSet;
import com.aiwsolutions.mongo.changes.TestConfiguration;
import com.aiwsolutions.mongo.changes.sample.ChangeSet1;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.rules.SpringClassRule;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by camhoang on 11/25/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RunnerConfiguration.class})
public class ClassChangeSetRunnerTest {

    @Autowired
    private ClassChangeSetRunner classChangeSetRunner;

    @Autowired
    private MongoDatabase mongoChanges_database;

    public static class ChangeSet0 extends ChangeSet {
        public static final String CHANGE_SET_COLLECTION = "class_change_set";
        public static final String DOCUMENT_KEY = "status";
        public static final String DOCUMENT_VALUE = "ok";

        @Override
        public void run() {
            getMongoDatabase().getCollection(CHANGE_SET_COLLECTION).insertOne(new Document(DOCUMENT_KEY, DOCUMENT_VALUE));
        }
    }

    @Test
    public void testRun() {
        assertThat(mongoChanges_database.getCollection(ChangeSet0.CHANGE_SET_COLLECTION).count(), is(0l));

        classChangeSetRunner.run(ChangeSet0.class);

        Document document = mongoChanges_database.getCollection(ChangeSet0.CHANGE_SET_COLLECTION).find().first();
        assertThat(document.get(ChangeSet0.DOCUMENT_KEY), is(ChangeSet0.DOCUMENT_VALUE));
    }
}
