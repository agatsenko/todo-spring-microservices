/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-30
 */
package io.agatsenko.todo.service.common.web.api.dto;

import java.lang.reflect.Type;

public interface DtoAssemblerProvider {
    <TModel, TDto> DtoAssembler<TModel, TDto> ensureGetAssembler(Type modelType, Type dtoType);
}
