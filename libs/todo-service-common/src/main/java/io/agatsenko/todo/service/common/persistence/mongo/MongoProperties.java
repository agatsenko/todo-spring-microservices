/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-30
 */
package io.agatsenko.todo.service.common.persistence.mongo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import lombok.Data;

@ConfigurationProperties(prefix = "persistence.mongodb")
@Data
public class MongoProperties {
    private String host;
    private int port;
    private String database;
    private String username;
    private char[] password;
}
