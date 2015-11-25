package com.aiwsolutions.mongo.changes;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Created by camhoang on 11/22/15.
 */
@ComponentScan("com.aiwsolutions.mongo.changes")
@PropertySource("mongochanges.properties")
public class TestConfiguration {

    @Bean
    static PropertySourcesPlaceholderConfigurer propertyPlaceHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public MongoClient mongoClient() {
        return new Fongo("test").getMongo();
    }
}
