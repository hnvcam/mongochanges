package com.aiwsolutions.mongo.changes;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
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

    @PostConstruct
    public void run() {
        if (!mongoChanges_autoRun) {
            LOGGER.info("Auto running is disabled.");
            return;
        }
    }
}
