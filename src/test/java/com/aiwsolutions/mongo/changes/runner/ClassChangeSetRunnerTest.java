package com.aiwsolutions.mongo.changes.runner;

import com.aiwsolutions.mongo.changes.TestConfiguration;
import com.aiwsolutions.mongo.changes.sample.ChangeSet2;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by camhoang on 11/25/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, RunnerConfiguration.class})
public class ClassChangeSetRunnerTest {

    @Autowired
    private ClassChangeSetRunner classChangeSetRunner;

    @Autowired
    private MongoDatabase mongoChanges_database;

    @Test
    public void testRun() {
        assertThat(mongoChanges_database.getCollection(ChangeSet2.CHANGE_SET_COLLECTION).count(), is(0l));

        classChangeSetRunner.run(Arrays.asList(ChangeSet2.class));

        Document document = mongoChanges_database.getCollection(ChangeSet2.CHANGE_SET_COLLECTION).find().first();
        assertThat(document.get(ChangeSet2.DOCUMENT_KEY), is(ChangeSet2.DOCUMENT_VALUE));
    }
}