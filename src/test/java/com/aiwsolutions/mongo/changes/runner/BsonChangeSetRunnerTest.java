package com.aiwsolutions.mongo.changes.runner;

import com.aiwsolutions.mongo.changes.ChangeSetExecutionException;
import com.aiwsolutions.mongo.changes.TestConfiguration;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Created by camhoang on 11/25/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RunnerConfiguration.class})
public class BsonChangeSetRunnerTest {

    @Autowired
    private BsonChangeSetRunner bsonChangeSetRunner;

    @Autowired
    private MongoDatabase mongoChanges_database;

    @Test
    public void testRun() throws URISyntaxException, IOException, ChangeSetExecutionException {
        mongoChanges_database.getCollection("sample").insertOne(new Document("status", "failed"));
        assertThat(mongoChanges_database.getCollection("sample").count(), is(1l));

        bsonChangeSetRunner.run(new FileSystemResource(new File(ClassLoader.getSystemResource("changes/changeSet2.bson").toURI())));

        Document document = mongoChanges_database.getCollection("sample").find().first();
        assertThat(document.get("status"), is("ok"));
    }

}
