package guru.springframework.reactivemongo.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import java.util.List;

/**
 * @author john
 * @since 13/10/2024
 */
public class MongoConfig extends AbstractReactiveMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "sfg";
    }

    @Bean
    MongoClient mongoClient() {
        return MongoClients.create();
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        builder.credential(MongoCredential.createCredential("root", "admin",
                "example.com".toCharArray()))
                .applyToClusterSettings(settings ->
                        settings.hosts(List.of(new ServerAddress("localhost", 27017))));
    }
}
