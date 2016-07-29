package corgi.storage.core.datasource;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
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
    public MongoDbFactory mongoDbFactory() throws Exception{
        //UserCredentials userCredentials = new UserCredentials("lake", "lake");
        return new SimpleMongoDbFactory(new MongoClient(), "lake");
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception{
        return new MongoTemplate(mongoDbFactory());
    }
}
