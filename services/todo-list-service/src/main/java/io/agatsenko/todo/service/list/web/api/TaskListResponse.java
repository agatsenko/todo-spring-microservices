/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.web.api;

import java.util.Collection;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

@Value
public class TaskListResponse {
    public final UUID id;
    public final long version;
    public final String name;
    public final Collection<TaskResponse> tasks;

    @JsonCreator
    @Builder
    public TaskListResponse(
            @JsonProperty("id") UUID id,
            @JsonProperty("version") long version,
            @JsonProperty("name") String name,
            @JsonProperty("tasks") Collection<TaskResponse> tasks) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.tasks = tasks;
    }
}
