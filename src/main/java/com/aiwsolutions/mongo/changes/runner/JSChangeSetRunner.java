package com.aiwsolutions.mongo.changes.runner;

import com.mongodb.Mongo;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.RawBsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by camhoang on 11/23/15.
 */
@Component
public class JSChangeSetRunner {
    private static final Logger LOGGER = Logger.getLogger(JSChangeSetRunner.class.getName());

    @Autowired
    private MongoDatabase mongoChanges_database;

    public void run(List<File> changeSets) {
        changeSets.stream().forEach(file -> runFile(file));
    }

    private void runFile(File file) {
        try {
            RawBsonDocument rawBsonDocument = new RawBsonDocument(Files.readAllBytes(file.toPath()));
            Document result = mongoChanges_database.runCommand(rawBsonDocument);
            if (result.getInteger("ok", -1) != 1) {
                LOGGER.log(Level.SEVERE, "Unable to execute change set {}", file.getName());
                return;
            }
            String writeErrors = result.getString("writeErrors");
            if (!StringUtils.isEmpty(writeErrors)) {
                LOGGER.log(Level.SEVERE, "Change set {} was executed with errors {}", new String[] {file.getName(), writeErrors});
            }
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
        }
    }
}
