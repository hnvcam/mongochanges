package com.aiwsolutions.mongo.changes.runner;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by camhoang on 11/25/15.
 */
@ComponentScan("com.aiwsolutions.mongo.changes.runner")
public class RunnerConfiguration {

    private MongoClient mongoClient() {
        return new Fongo("runner").getMongo();
    }

    @Bean
    public MongoDatabase mongoDatabase() {
        return mongoClient().getDatabase("test");
    }
}
