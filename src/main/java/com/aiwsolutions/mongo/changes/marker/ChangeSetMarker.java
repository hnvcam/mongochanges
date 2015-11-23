package com.aiwsolutions.mongo.changes.marker;

import com.aiwsolutions.mongo.changes.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

/**
 * Created by choang on 11/23/15.
 */
@Component
public class ChangeSetMarker {
    private static final Logger LOGGER = Logger.getLogger(ChangeSetMarker.class.getName());

    private static final String MARKER_COLLECTION = "__mongochanges_changeset_marker";

    @Autowired
    private MongoDatabase mongoDatabase;

    private Map<String, Document> documentMap = new ConcurrentHashMap<>();

    public boolean isRunnable(File file) {
        return isRunnable(file.getName());
    }

    public boolean isRunnable(Class<? extends ChangeSet> clazz) {
        return isRunnable(clazz.getName());
    }

    private boolean isRunnable(String name) {
        return mongoDatabase.getCollection(MARKER_COLLECTION).find(eq("name", name)).first().getBoolean("success");
    }

    public boolean start(File file) {
        return start(file.getName());
    }

    private boolean start(String name) {
        Document document = documentMap.get(name);

        // fetching from DB.
        if (document == null) {
            document = mongoDatabase.getCollection(MARKER_COLLECTION).find(eq("name", name)).first();
        }

        if (document != null || document.getDate("started") != null) {
            LOGGER.log(Level.INFO, "Change set {} already started. Skipping!", name);
            return false;
        }
        document = new Document("name", name).append("started", new Date());
        documentMap.put(name, document);
        return true;
    }
}
