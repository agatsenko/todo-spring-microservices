/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.model;

import java.util.UUID;
import javax.validation.constraints.NotNull;

public interface UpdateTaskSpec {
    @NotNull
    UUID getId();

    @NotNull
    String getDescription();

    Boolean getCompleted();

    Integer getOrder();
}
