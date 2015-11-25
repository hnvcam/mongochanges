package com.aiwsolutions.mongo.changes.runner;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * Created by camhoang on 11/25/15.
 */
public class RunnerConfiguration {

    @Autowired
    private MongoClient mongoClient;

    @Bean
    public MongoDatabase mongoDatabase() {
        return mongoClient.getDatabase("test");
    }
}
