package com.dounine.corgi.mongo.boot.datasource;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.SourceFilteringListener;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Component;

/**
 * Created by huanghuanlai on 16/4/29.
 */
@Component
public class MongoDbSource {
    @Autowired
    private Environment env;

    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        MongoClientOptions.Builder mongoOperations = MongoClientOptions.builder();
        mongoOperations.socketTimeout(1000 * 2);
        mongoOperations.connectTimeout(1000 * 2);
        ServerAddress serverAddress = new ServerAddress(env.getProperty("db.host"), Integer.valueOf(env.getProperty("db.post")));
        MongoClientOptions mo = mongoOperations.build();
        MongoClient mongoClient = new MongoClient(serverAddress, mo);
        return new SimpleMongoDbFactory(mongoClient, env.getProperty("db.name"));
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}
