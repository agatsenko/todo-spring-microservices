/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.web.api;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import io.agatsenko.todo.service.list.model.NewTaskListSpec;
import io.agatsenko.todo.service.list.model.NewTaskSpec;

@Value
public class NewTaskListRequest implements NewTaskListSpec {
    public final String name;
    public final Collection<NewTaskRequest> tasks;

    @JsonCreator
    @Builder
    public NewTaskListRequest(
            @JsonProperty("name") String name,
            @JsonProperty("tasks") Collection<NewTaskRequest> tasks) {
        this.name = name;
        this.tasks = tasks;
    }

    @Override
    public Collection<? extends NewTaskSpec> getTasks() {
        return tasks;
    }
}
