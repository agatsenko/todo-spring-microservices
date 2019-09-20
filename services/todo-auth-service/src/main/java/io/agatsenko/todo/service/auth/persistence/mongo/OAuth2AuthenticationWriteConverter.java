/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.auth.persistence.mongo;

import org.apache.commons.lang.SerializationUtils;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class OAuth2AuthenticationWriteConverter implements Converter<OAuth2Authentication, Binary> {
    @Override
    public Binary convert(OAuth2Authentication auth) {
        if (auth == null) {
            return null;
        }
        return new Binary(BsonBinarySubType.BINARY, SerializationUtils.serialize(auth));
    }
}
