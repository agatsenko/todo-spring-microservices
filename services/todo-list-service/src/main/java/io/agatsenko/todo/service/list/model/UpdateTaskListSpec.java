/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.model;

import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface UpdateTaskListSpec {
    @NotNull
    UUID getId();

    @NotNull
    Long getVersion();

    @NotBlank
    String getName();

    @Valid
    Collection<? extends NewTaskSpec> getNewTasks();

    @Valid
    Collection<? extends UpdateTaskSpec> getUpdateTasks();

    @Valid
    Collection<? extends RemoveTaskSpec> getRemoveTasks();
}
