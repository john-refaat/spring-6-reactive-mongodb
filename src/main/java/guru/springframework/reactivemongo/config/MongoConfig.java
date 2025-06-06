package guru.springframework.reactivemongo.config;

import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;

import java.util.List;

/**
 * @author john
 * @since 13/10/2024
 */
@Configuration
@EnableReactiveMongoAuditing
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Value("${sfg.mongo.host}")
    private String host;

    @Value("${sfg.mongo.port}")
    private Integer port;

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
                        settings.hosts(List.of(new ServerAddress(host, port))));
    }
}
