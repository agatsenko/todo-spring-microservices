/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.model;

import java.util.*;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import io.agatsenko.todo.service.common.service.locator.Services;

@Document("task_lists")
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class TaskList {
    @Id
    private final UUID id;

    @Version
    private long version;

    private final UUID userId;

    private final String name;

    private final Set<Task> tasks;

    @Builder(toBuilder = true)
    public TaskList(
            @NonNull UUID id,
            long version,
            @NonNull UUID userId,
            @NonNull String name,
            @Singular @NonNull Collection<Task> tasks) {
        this.id = id;
        this.version = version;
        this.userId = userId;
        this.name = name;
        this.tasks = Set.copyOf(tasks);
    }

    public TaskList addTasks(Collection<? extends NewTaskSpec> specs) {
        if (specs == null || specs.isEmpty()) {
            return this;
        }
        validate(specs);
        final var newTasks = specs.stream().map(this::createTask).collect(Collectors.toList());
        newTasks.addAll(this.tasks);
        return toBuilder().clearTasks().tasks(newTasks).build();
    }

    public TaskList updateTasks(Collection<? extends UpdateTaskSpec> specs) {
        if (specs == null || specs.isEmpty()) {
            return this;
        }
        validate(specs);
        List<Task> replaceTasks = new ArrayList<>(tasks.size());
        for (val task : tasks) {
            replaceTasks.add(
                    specs.stream()
                            .filter(spec -> Objects.equals(spec.getId(), task.getId()))
                            .findAny()
                            .map(spec -> updateTask(task, spec))
                            .orElse(task)
            );
        }
        return toBuilder().clearTasks().tasks(replaceTasks).build();
    }

    public TaskList removeTasks(Collection<? extends RemoveTaskSpec> specs) {
        if (specs == null || specs.isEmpty() || tasks.isEmpty()) {
            return this;
        }
        validate(specs);
        Map<UUID, Task> replaceTaskMap = tasks.stream().collect(Collectors.toMap(Task::getId, t -> t));
        for (final var spec : specs) {
            replaceTaskMap.remove(spec.getId());
        }
        return tasks.size() == replaceTaskMap.size() ? this : toBuilder().clearTasks().tasks(replaceTaskMap.values()).build();
    }

    private static Validator getValidator() {
        return Services.locator().get(Validator.class);
    }

    private void validate(Object value) {
        final var violations = getValidator().validate(value);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    private Task createTask(NewTaskSpec spec) {
        return Task.builder()
                .id(UUID.randomUUID())
                .description(spec.getDescription())
                .completed(spec.getCompleted() == null ? false : spec.getCompleted())
                .order(spec.getOrder() == null ? Integer.MAX_VALUE : spec.getOrder())
                .build();
    }

    private Task updateTask(Task srcTask, UpdateTaskSpec spec) {
        return srcTask.toBuilder()
                .description(spec.getDescription())
                .completed(spec.getCompleted() == null ? false : spec.getCompleted())
                .order(spec.getOrder() == null ? Integer.MAX_VALUE : spec.getOrder())
                .build();
    }
}
