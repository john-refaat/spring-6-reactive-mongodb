package guru.springframework.reactivemongo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HttpLogFormatter;
import org.zalando.logbook.Sink;
import org.zalando.logbook.json.JsonHttpLogFormatter;
import org.zalando.logbook.logstash.LogstashLogbackSink;

/**
 * Author:john
 * Date:08/02/2025
 * Time:03:15
 */
@Configuration
public class LogbookConfig {

    @Bean
    public Sink logbookLogStash() {
        HttpLogFormatter formatter = new JsonHttpLogFormatter();
        return new LogstashLogbackSink(formatter);
    }
}
