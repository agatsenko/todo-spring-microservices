/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-23
 */
package io.agatsenko.todo.service.list.web.api;

import java.util.Collection;

import io.agatsenko.todo.service.common.web.api.dto.BaseDtoAssembler;
import io.agatsenko.todo.service.list.model.Task;
import io.agatsenko.todo.service.list.model.TaskList;

public class TaskListResponseAssembler extends BaseDtoAssembler<TaskList, TaskListResponse>{
    public TaskListResponseAssembler() {
        super(TaskList.class, TaskListResponse.class);
    }

    @Override
    protected TaskListResponse assemblyNotNull(TaskList list) {
        return TaskListResponse.builder()
                .id(list.getId())
                .version(list.getVersion())
                .name(list.getName())
                .tasks(assemblyTasks(list))
                .build();
    }

    private Collection<TaskResponse> assemblyTasks(TaskList list) {
        return getAssemblers().assembly(Task.class, TaskResponse.class, list.getTasks());
    }
}
