/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-23
 */
package io.agatsenko.todo.service.list.web.api;

import java.util.Collection;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import io.agatsenko.todo.service.list.model.UpdateTaskListSpec;

@Value
public class UpdateTaskListRequest implements UpdateTaskListSpec {
    public final UUID id;
    public final Long version;
    public final String name;
    public final Collection<NewTaskRequest> newTasks;
    public final Collection<UpdateTaskRequest> updateTasks;
    public final Collection<RemoveTaskRequest> removeTasks;

    @JsonCreator
    @Builder
    public UpdateTaskListRequest(
            @JsonProperty("id") UUID id,
            @JsonProperty("version") Long version,
            @JsonProperty("name") String name,
            @JsonProperty("newTasks") Collection<NewTaskRequest> newTasks,
            @JsonProperty("updateTasks") Collection<UpdateTaskRequest> updateTasks,
            @JsonProperty("removeTasks") Collection<RemoveTaskRequest> removeTasks) {
        this.id = id;
        this.version = version;
        this.name = name;
        this.newTasks = newTasks;
        this.updateTasks = updateTasks;
        this.removeTasks = removeTasks;
    }
}
