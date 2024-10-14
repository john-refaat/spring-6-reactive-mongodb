package guru.springframework.reactivemongo.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;

import java.util.List;

/**
 * @author john
 * @since 13/10/2024
 */
@Configuration
public class MongoConfig extends AbstractReactiveMongoConfiguration {
    @Override
    protected String getDatabaseName() {
        return "sfg";
    }

    @Bean
    MongoClient reactiMongoClient() {
        return MongoClients.create();
    }

    @Override
    protected void configureClientSettings(MongoClientSettings.Builder builder) {
        builder.credential(MongoCredential.createCredential("root", "admin",
                "example".toCharArray()))
                .applyToClusterSettings(settings ->
                        settings.hosts(List.of(new ServerAddress("127.0.0.1", 27017))));
    }
}
