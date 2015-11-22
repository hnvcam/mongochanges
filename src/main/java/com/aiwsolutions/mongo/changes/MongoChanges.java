package com.aiwsolutions.mongo.changes;

import org.springframework.beans.factory.InitializingBean;
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

    private String scriptChangeSetPath;

    private String javaChangeSetPath;

    private boolean autoRun = false;

    @PostConstruct
    public void run() {
        if (!autoRun) {
            LOGGER.info("Auto running is disabled.");
            return;
        }
    }

    public boolean isAutoRun() {
        return autoRun;
    }

    public MongoChanges setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
        return this;
    }

    public String getJavaChangeSetPath() {
        return javaChangeSetPath;
    }

    public MongoChanges setJavaChangeSetPath(String javaChangeSetPath) {
        this.javaChangeSetPath = javaChangeSetPath;
        return this;
    }

    public String getScriptChangeSetPath() {
        return scriptChangeSetPath;
    }

    public MongoChanges setScriptChangeSetPath(String scriptChangeSetPath) {
        this.scriptChangeSetPath = scriptChangeSetPath;
        return this;
    }
}
