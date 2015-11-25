package com.aiwsolutions.mongo.changes;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by camhoang on 11/23/15.
 */
@Configuration
public class MongoChangesConfig {
    @Value("${mongo.changes.script.path:changeset}")
    private String scriptPath;

    @Value("${mongo.changes.class.path}")
    private String classPath;

    @Value("${mongo.changes.autorun:true}")
    private String autoRun;

    @Value("${mongo.changes.client.bean.name:mongoClient}")
    private String clientName;

    @Value("${mongo.changes.database.name}")
    private String dbName;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public String mongoChanges_scriptChangeSetPath() {
        return scriptPath;
    }

    @Bean
    public String mongoChanges_javaChangeSetPath() {
        return classPath;
    }

    @Bean
    public Boolean mongoChanges_autoRun() {
        return Boolean.valueOf(autoRun);
    }

    @Bean
    public MongoDatabase mongoChanges_database() {
        MongoClient mongoClient = applicationContext.getBean(clientName, MongoClient.class);
        return mongoClient.getDatabase(dbName);
    }

}
