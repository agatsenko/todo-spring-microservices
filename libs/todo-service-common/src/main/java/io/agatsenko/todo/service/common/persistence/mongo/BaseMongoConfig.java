/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-30
 */
package io.agatsenko.todo.service.common.persistence.mongo;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

import io.agatsenko.todo.service.common.persistence.mongo.converter.UuidToBinaryConverter;

@EnableConfigurationProperties(MongoProperties.class)
public abstract class BaseMongoConfig extends AbstractMongoConfiguration {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MongoProperties mongoProperties() {
        return new MongoProperties();
    }

    @Override
    public MongoClient mongoClient() {
        final var mongoProps = mongoProperties();
        return new MongoClient(
                new ServerAddress(mongoProps.getHost(), mongoProps.getPort()),
                MongoCredential.createCredential(
                        mongoProps.getUsername(),
                        mongoProps.getDatabase(),
                        mongoProps.getPassword()
                ),
                MongoClientOptions.builder()
                        .build()
        );
    }

    @Override
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = List.of(
                new UuidToBinaryConverter()
        );
        return new MongoCustomConversions(converters);
    }

    @Override
    protected String getDatabaseName() {
        return mongoProperties().getDatabase();
    }
}
