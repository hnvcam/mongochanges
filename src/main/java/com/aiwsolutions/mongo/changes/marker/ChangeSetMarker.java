package com.aiwsolutions.mongo.changes.marker;

import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public boolean start(String name) {
        Document document = documentMap.get(name);
        boolean dirtyData = false;

        // fetching from DB.
        if (document == null) {
            document = mongoDatabase.getCollection(MARKER_COLLECTION).find(eq("name", name)).first();
            dirtyData = document != null;
        }

        if (document != null && document.getDate("start") != null) {
            LOGGER.log(Level.INFO, "Change set \"{0}\" already started. Skipping!", name);
            return false;
        }

        // remove if any dirty data
        if (dirtyData) {
            mongoDatabase.getCollection(MARKER_COLLECTION).findOneAndDelete(eq("name", name));
        }

        document = new Document("name", name).append("start", new Date());
        documentMap.put(name, document);
        LOGGER.log(Level.INFO, "Executing change set \"{0}\"...", name);
        return true;
    }

    public void end(String name) {
        Document document = documentMap.get(name);

        if (document == null && document.getDate("started") != null) {
            throw new IllegalStateException(String.format("Change set \"%s\" was not started.", name));
        }

        document.put("end", new Date());
        mongoDatabase.getCollection(MARKER_COLLECTION).insertOne(document);
        documentMap.remove(name);
        LOGGER.log(Level.INFO, "Accomplished change set \"{0}\"", name);
    }

    public void clear(String name) {
        mongoDatabase.getCollection(MARKER_COLLECTION).findOneAndDelete(new Document("name", name));
        documentMap.remove(name);
    }
}
