/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-23
 */
package io.agatsenko.todo.service.list.web.api;

import io.agatsenko.todo.service.common.web.api.dto.BaseDtoAssembler;
import io.agatsenko.todo.service.list.model.Task;

public class TaskResponseAssembler extends BaseDtoAssembler<Task, TaskResponse> {
    public TaskResponseAssembler() {
        super(Task.class, TaskResponse.class);
    }

    @Override
    protected TaskResponse assemblyNotNull(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .description(task.getDescription())
                .completed(task.isCompleted())
                .order(task.getOrder())
                .build();
    }
}
