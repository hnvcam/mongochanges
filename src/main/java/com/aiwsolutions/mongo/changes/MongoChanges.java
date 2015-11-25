package com.aiwsolutions.mongo.changes;

import com.aiwsolutions.mongo.changes.marker.ChangeSetMarker;
import com.aiwsolutions.mongo.changes.runner.BsonChangeSetRunner;
import com.aiwsolutions.mongo.changes.runner.ClassChangeSetRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by camhoang on 11/22/15.
 */
@Component
public class MongoChanges {
    private static final Logger LOGGER = Logger.getLogger(MongoChanges.class.getName());

    @Autowired
    private String mongoChanges_scriptChangeSetPath;

    @Autowired
    private String mongoChanges_javaChangeSetPath;

    @Autowired
    private Boolean mongoChanges_autoRun = true;

    @Autowired
    private ChangeSetLoader changeSetLoader;

    @Autowired
    private ChangeSetMarker changeSetMarker;

    @Autowired
    private BsonChangeSetRunner bsonChangeSetRunner;

    @Autowired
    private ClassChangeSetRunner classChangeSetRunner;

    private TreeMap<String, Object> changeSets = new TreeMap<>();

    private int executedChangeSet = 0;

    private int skippedChangeSet = 0;

    @PostConstruct
    public void autoRun() throws ChangeSetExecutionException {
        if (!mongoChanges_autoRun) {
            LOGGER.info("Auto running is disabled.");
            return;
        }
        executeChangeSets();
    }

    public void executeChangeSets() throws ChangeSetExecutionException {
        reset();
        try {
            loadChangeSets();
            executeChangeSetSequentially();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Aborted! Skipped: {0}. Executed: {1}. Exception: {2}",
                    new String [] {String.valueOf(skippedChangeSet), String.valueOf(executedChangeSet), e.getMessage()});
            throw new ChangeSetExecutionException(e.getMessage(), e);
        }
    }

    private void executeChangeSetSequentially() throws IOException, ChangeSetExecutionException {
        for (Map.Entry<String, Object> entry : changeSets.entrySet()) {
            String changeSetName = entry.getKey();
            if (changeSetMarker.start(changeSetName)) {
                Object changeSet = entry.getValue();
                try {
                    if (changeSet instanceof Resource) {
                        bsonChangeSetRunner.run((Resource) changeSet);
                    } else {
                        classChangeSetRunner.run((Class<? extends ChangeSet>) changeSet);
                    }
                    changeSetMarker.end(changeSetName);
                    executedChangeSet++;
                } catch (ChangeSetExecutionException e) {
                    LOGGER.log(Level.SEVERE, "Failed to execute change set \"{0}\" with error {1}", new String[]{changeSetName, e.getError()});
                    changeSetMarker.clear(changeSetName);
                    throw e;
                }
            } else {
                skippedChangeSet++;
            }
        }
    }

    private void loadChangeSets() throws IOException {
        changeSetLoader.loadChangeSetClassesFromPackage(mongoChanges_javaChangeSetPath)
                .stream()
                .forEachOrdered(clazz -> changeSets.put(clazz.getSimpleName(), clazz));

        changeSetLoader.loadChangeSetBsonResourcesFromPath(mongoChanges_scriptChangeSetPath)
                .stream()
                .forEachOrdered(resource -> changeSets.put(resource.getFilename(), resource));

        LOGGER.log(Level.INFO, "Loaded {0} change sets", changeSets.size());
    }

    private void reset() {
        changeSets.clear();
        executedChangeSet = 0;
        skippedChangeSet = 0;
    }

    public int getExecutedChangeSet() {
        return executedChangeSet;
    }

    public int getSkippedChangeSet() {
        return skippedChangeSet;
    }
}
