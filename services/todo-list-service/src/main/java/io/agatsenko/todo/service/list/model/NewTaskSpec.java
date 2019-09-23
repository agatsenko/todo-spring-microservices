/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.list.model;

import javax.validation.constraints.NotEmpty;

public interface NewTaskSpec {
    @NotEmpty
    String getDescription();

    Boolean getCompleted();

    Integer getOrder();
}
