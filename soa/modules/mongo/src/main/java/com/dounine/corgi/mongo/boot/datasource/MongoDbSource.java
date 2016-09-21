package com.dounine.corgi.mongo.boot.datasource;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.SourceFilteringListener;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.stereotype.Component;

/**
 * Created by huanghuanlai on 16/4/29.
 */
@Component
public class MongoDbSource {
    @Bean
    public MongoDbFactory mongoDbFactory() throws Exception {
        //UserCredentials userCredentials = new UserCredentials("lake", "lake");

        // link for user
//        ServerAddress serverAddress = new ServerAddress("localhost",27017);
//        MongoCredential mongoCredential = MongoCredential.createMongoCRCredential("root","lake","root".toCharArray());
//        List<MongoCredential> mongoCredentials = new ArrayList<>(0);
//        mongoCredentials.add(mongoCredential);
//        MongoClient mongoClient = new MongoClient(serverAddress,mongoCredentials);
        MongoClientOptions.Builder mongoOperations = MongoClientOptions.builder();
        mongoOperations.socketTimeout(1000 * 2);
        mongoOperations.connectTimeout(1000 * 2);
        ServerAddress serverAddress = new ServerAddress("127.0.0.1", 27016);
        MongoClientOptions mo = mongoOperations.build();
        MongoClient mongoClient = new MongoClient(serverAddress, mo);
        return new SimpleMongoDbFactory(mongoClient, "sso");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}