/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-20
 */
package io.agatsenko.todo.service.auth.persistence.mongo;

import org.apache.commons.lang.SerializationUtils;
import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

public class OAuth2AuthenticationReadConverter implements Converter<Binary, OAuth2Authentication> {
    @Override
    public OAuth2Authentication convert(Binary bin) {
        return (OAuth2Authentication) SerializationUtils.deserialize(bin.getData());
    }
}
