/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.list.model.NewTaskListSpec;
import io.agatsenko.todo.service.list.model.TaskList;
import io.agatsenko.todo.service.list.model.TaskListRepo;
import io.agatsenko.todo.service.list.model.UpdateTaskListSpec;

@RequiredArgsConstructor
public class TaskListService {
    private final TaskListRepo listRepo;
    private final Validator validator;

    public Collection<TaskList> getTaskLists(UUID userId) {
        return listRepo.findByUserId(userId);
    }

    public Optional<TaskList> getTaskList(UUID userId, UUID listId) {
        return listRepo.findByUserIdAndId(userId, listId);
    }


    public TaskList addTaskList(@NonNull UUID userId, @NonNull NewTaskListSpec spec) {
        validate(spec);
        final var list = TaskList.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .name(spec.getName())
                .build()
                .addTasks(spec.getTasks());
        return listRepo.save(list);
    }

    public Optional<TaskList> updateTaskList(@NonNull UUID userId, @NonNull UpdateTaskListSpec spec) {
        validate(spec);
        return listRepo.findByUserIdAndIdAndVersion(userId, spec.getId(), spec.getVersion())
                .map(list -> list.toBuilder()
                        .name(spec.getName())
                        .build()
                        .addTasks(spec.getNewTasks())
                        .updateTasks(spec.getUpdateTasks())
                        .removeTasks(spec.getRemoveTasks())
                )
                .map(listRepo::save);
    }

    public boolean removeTaskList(UUID userId, UUID listId, long version) {
        if (listRepo.existsByUserIdAndIdAndVersion(userId, listId, version)) {
            listRepo.deleteByUserIdAndIdAndVersion(userId, listId, version);
            return true;
        }
        return false;
    }

    private void validate(Object obj) {
        final var violations = validator.validate(obj);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
