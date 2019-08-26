/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-07-22
 */
package io.agatsenko.todo.service.common.mongo;

import java.nio.ByteBuffer;
import java.util.UUID;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.core.convert.converter.Converter;

public class UuidToBindDataConverter implements Converter<UUID, Binary> {
    @Override
    public Binary convert(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        var bytes = ByteBuffer.wrap(new byte[16]);
        bytes.putLong(uuid.getMostSignificantBits());
        bytes.putLong(uuid.getLeastSignificantBits());
        return new Binary(BsonBinarySubType.UUID_STANDARD, bytes.array());
    }
}
