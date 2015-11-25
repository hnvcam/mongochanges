package com.aiwsolutions.mongo.changes.runner;

import com.aiwsolutions.mongo.changes.TestConfiguration;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by camhoang on 11/25/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class, RunnerConfiguration.class})
public class BsonChangeSetRunnerTest {

    @Autowired
    private BsonChangeSetRunner bsonChangeSetRunner;

    @Autowired
    private MongoDatabase mongoChanges_database;

    @Test
    public void testRun() throws URISyntaxException {
        assertThat(mongoChanges_database.getCollection("sample").count(), is(0l));

        bsonChangeSetRunner.run(Arrays.asList(new File(ClassLoader.getSystemResource("changes/changeSet1.bson").toURI())));

        Document document = mongoChanges_database.getCollection("sample").find().first();
        assertThat(document.get("status"), is("ok"));
    }

}