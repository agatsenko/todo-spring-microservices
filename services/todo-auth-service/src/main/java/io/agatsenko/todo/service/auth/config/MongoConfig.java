/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-30
 */
package io.agatsenko.todo.service.auth.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import io.agatsenko.todo.service.auth.persistence.mongo.OAuth2AuthenticationReadConverter;
import io.agatsenko.todo.service.auth.persistence.mongo.OAuth2AuthenticationWriteConverter;
import io.agatsenko.todo.service.common.persistence.mongo.BaseMongoConfig;
import io.agatsenko.todo.service.common.persistence.mongo.converter.UuidToBinaryConverter;

@Configuration
public class MongoConfig extends BaseMongoConfig {
    @Override
    public MongoCustomConversions customConversions() {
        List<Converter<?, ?>> converters = List.of(
                new UuidToBinaryConverter(),
                new OAuth2AuthenticationReadConverter(),
                new OAuth2AuthenticationWriteConverter()
        );
        return new MongoCustomConversions(converters);
    }
}
