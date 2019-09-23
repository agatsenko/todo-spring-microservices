/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.model;

import java.util.Collection;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

public interface NewTaskListSpec {
    @NotBlank
    String getName();

    @Valid
    Collection<? extends NewTaskSpec> getTasks();
}
