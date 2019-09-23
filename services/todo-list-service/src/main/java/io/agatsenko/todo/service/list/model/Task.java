/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.model;

import java.util.UUID;

import org.springframework.data.mongodb.core.mapping.Field;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Task {
    @Field("id")
    private final UUID id;
    private final String description;
    private final boolean completed;
    private final int order;
}
