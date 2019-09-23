/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-23
 */
package io.agatsenko.todo.service.list.web.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import io.agatsenko.todo.service.list.model.RemoveTaskSpec;

@Value
public class RemoveTaskRequest implements RemoveTaskSpec {
    public final UUID id;

    @JsonCreator
    public RemoveTaskRequest(@JsonProperty("id") UUID id) {
        this.id = id;
    }
}
