/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.web.api;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
public class TaskResponse {
    public final UUID id;
    public final String description;
    public final boolean completed;
    public final int order;

    @JsonCreator
    @Builder
    public TaskResponse(
            @JsonProperty("id") UUID id,
            @JsonProperty("description") String description,
            @JsonProperty("completed") boolean completed,
            @JsonProperty("order") int order) {
        this.id = id;
        this.description = description;
        this.completed = completed;
        this.order = order;
    }
}
