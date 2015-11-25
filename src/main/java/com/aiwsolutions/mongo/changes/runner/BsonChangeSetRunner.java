package com.aiwsolutions.mongo.changes.runner;

import com.aiwsolutions.mongo.changes.ChangeSetExecutionException;
import com.mongodb.client.MongoDatabase;
import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by camhoang on 11/23/15.
 */
@Component
public class BsonChangeSetRunner {
    private static final Logger LOGGER = Logger.getLogger(BsonChangeSetRunner.class.getName());

    private static final String COMMANDS_KEY = "commands";

    @Autowired
    private MongoDatabase mongoChanges_database;

    public void run(Resource resource) throws IOException, ChangeSetExecutionException {
        String resourceData = FileCopyUtils.copyToString(new InputStreamReader(resource.getInputStream()));
        if (StringUtils.isEmpty(resourceData)) {
            LOGGER.log(Level.INFO, "Empty change set \"{0}\"", resource.getFilename());
            return;
        }
        LOGGER.log(Level.FINEST, "Read resource {0}: {1}", new String[]{resource.getFilename(), resourceData});
        BsonDocument resourceCommands = BsonDocument.parse(resourceData);
        for (BsonDocument command : getCommands(resourceCommands)) {
            executeCommand(resource, command);
        }
    }

    private List<BsonDocument> getCommands(BsonDocument resourceCommands) {
        if (resourceCommands.containsKey(COMMANDS_KEY)) {
            return resourceCommands.getArray(COMMANDS_KEY)
                    .stream()
                    .map(bsonValue -> bsonValue.asDocument())
                    .collect(Collectors.toList());
        } else {
            return Arrays.asList(resourceCommands);
        }
    }

    private void executeCommand(Resource resource, BsonDocument command) throws ChangeSetExecutionException {
        BsonDocument result = mongoChanges_database.runCommand(command, BsonDocument.class);
        if (result.containsKey("ok")) {
            if (result.getInt32("ok").getValue() != 1) {
                String error = result.get("writeErrors").toString();
                LOGGER.log(Level.SEVERE, "Failed to execute change set \"{0}\": {1}",
                        new String[]{resource.getFilename(), error});
                throw new ChangeSetExecutionException(error);
            }
        } else if (result.containsKey("n")) {
            if (result.getInt32("n").getValue() == 0) {
                LOGGER.log(Level.INFO, "Executed change set \"{0}\" with no changes", resource.getFilename());
            }
        }
    }
}
