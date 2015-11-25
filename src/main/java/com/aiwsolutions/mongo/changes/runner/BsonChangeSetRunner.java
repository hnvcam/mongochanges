package com.aiwsolutions.mongo.changes.runner;

import com.aiwsolutions.mongo.changes.ChangeSetExecutionException;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by camhoang on 11/23/15.
 */
@Component
public class BsonChangeSetRunner {
    private static final Logger LOGGER = Logger.getLogger(BsonChangeSetRunner.class.getName());

    @Autowired
    private MongoDatabase mongoChanges_database;

    public void run(File file) throws IOException {
        BsonDocument command = BsonDocument.parse(String.join("", Files.readAllLines(file.toPath())));
        BsonDocument result = mongoChanges_database.runCommand(command, BsonDocument.class);
        if (result.containsKey("ok")) {
            if (result.getInt32("ok").getValue() != 1) {
                String error = result.get("writeErrors").toString();
                LOGGER.log(Level.SEVERE, "Failed to execute change set \"{0}\": {1}",
                        new String[]{file.getName(), error});
                throw new ChangeSetExecutionException(file.getName(), error);
            }
        } else if (result.containsKey("n")) {
            if (result.getInt32("n").getValue() == 0) {
                LOGGER.log(Level.INFO, "Executed change set \"{0}\" with no changes", file.getName());
            }
        }
    }
}
