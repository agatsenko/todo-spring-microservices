/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.web.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import io.agatsenko.todo.service.list.model.NewTaskSpec;

@Value
public class NewTaskRequest implements NewTaskSpec {
    public final String description;
    public final Boolean completed;
    public final Integer order;

    @JsonCreator
    @Builder
    public NewTaskRequest(
            @JsonProperty("description") String description,
            @JsonProperty("completed") Boolean completed,
            @JsonProperty("order") Integer order) {
        this.description = description;
        this.completed = completed;
        this.order = order;
    }
}
