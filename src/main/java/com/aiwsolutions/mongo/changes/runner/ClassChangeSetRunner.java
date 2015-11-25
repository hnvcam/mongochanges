package com.aiwsolutions.mongo.changes.runner;

import com.aiwsolutions.mongo.changes.ChangeSet;
import com.aiwsolutions.mongo.changes.ChangeSetExecutionException;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by camhoang on 11/23/15.
 */
@Component
public class ClassChangeSetRunner {
    private static final Logger LOGGER = Logger.getLogger(ClassChangeSetRunner.class.getName());

    @Autowired
    private MongoDatabase mongoChanges_database;

    public void run(Class<? extends ChangeSet> clazz) throws ChangeSetExecutionException {
        try {
            ChangeSet instance = clazz.newInstance();
            instance.setMongoDatabase(mongoChanges_database);
            instance.run();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to execute change set \"{0}\": {1}",
                    new String [] {clazz.getSimpleName(), e.getCause().toString()});
            throw new ChangeSetExecutionException(e.getMessage(), e);
        }
    }
}
