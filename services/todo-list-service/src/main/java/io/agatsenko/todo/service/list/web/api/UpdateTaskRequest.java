/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-23
 */
package io.agatsenko.todo.service.list.web.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import io.agatsenko.todo.service.list.model.UpdateTaskSpec;

@Value
public class UpdateTaskRequest implements UpdateTaskSpec {
    public final UUID id;
    public final String description;
    public final Boolean completed;
    public final Integer order;

    @JsonCreator
    @Builder
    public UpdateTaskRequest(
            @JsonProperty("id") UUID id,
            @JsonProperty("description") String description,
            @JsonProperty("completed") Boolean completed,
            @JsonProperty("order") Integer order) {
        this.id = id;
        this.description = description;
        this.completed = completed;
        this.order = order;
    }
}
