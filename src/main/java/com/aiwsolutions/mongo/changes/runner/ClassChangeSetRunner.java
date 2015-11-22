package com.aiwsolutions.mongo.changes.runner;

import com.aiwsolutions.mongo.changes.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by camhoang on 11/23/15.
 */
@Component
public class ClassChangeSetRunner {
    private static final Logger LOGGER = Logger.getLogger(ClassChangeSetRunner.class.getName());

    @Autowired
    MongoDatabase mongoDatabase;

    public void run(List<Class<? extends ChangeSet>> classes) {
        classes.stream().forEach(clazz -> runClass(clazz));
    }

    private void runClass(Class<? extends ChangeSet> clazz) {
        try {
            ChangeSet instance = clazz.newInstance();
            instance.setMongoDatabase(mongoDatabase);
            instance.run();
        } catch (InstantiationException e) {
            LOGGER.severe(e.getMessage());
        } catch (IllegalAccessException e) {
            LOGGER.severe(e.getMessage());
        }
    }
}
