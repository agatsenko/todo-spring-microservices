/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-30
 */
package io.agatsenko.todo.service.common.web.api.dto;

import java.lang.reflect.Type;

import org.apache.commons.lang3.reflect.TypeLiteral;
import org.apache.commons.lang3.reflect.Typed;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import io.agatsenko.todo.util.Check;

@EqualsAndHashCode(of = {"modelType", "dtoType"})
@ToString(of = {"modelType", "dtoType"})
public abstract class BaseDtoAssembler<TModel, TDto> implements DtoAssembler<TModel, TDto>, DtoAssemblerProviderAware {
    private final Type modelType;
    private final Type dtoType;

    private DtoAssemblerProvider dtoAssemblerProvider;

    private BaseDtoAssembler(Type modelType, Type dtoType) {
        Check.argNotNull(modelType, "modelType");
        Check.argNotNull(dtoType, "dtoType");
        this.modelType = modelType;
        this.dtoType = dtoType;
    }

    public BaseDtoAssembler(Class<TModel> modelType, Class<TDto> dtoType) {
        this((Type) modelType, dtoType);
    }

    public BaseDtoAssembler(Typed<TModel> modelType, Typed<TDto> dtoType) {
        this(modelType.getType(), dtoType.getType());
    }

    public BaseDtoAssembler(Class<TModel> modelType, TypeLiteral<TDto> dtoType) {
        this(modelType, dtoType.getType());
    }

    public BaseDtoAssembler(TypeLiteral<TModel> modelType, Class<TDto> dtoType) {
        this(modelType.getType(), dtoType);
    }

    protected abstract TDto assemblyNotNull(TModel model);

    @Override
    public final Type getModelType() {
        return modelType;
    }

    @Override
    public final Type getDtoType() {
        return dtoType;
    }

    public DtoAssemblerProvider getDtoAssemblerProvider() {
        return dtoAssemblerProvider;
    }

    @Override
    public void setDtoAssemblerProvider(DtoAssemblerProvider dtoAssemblerProvider) {
        this.dtoAssemblerProvider = dtoAssemblerProvider;
    }

    @Override
    public final TDto assembly(TModel model) {
        return model == null ? null : assemblyNotNull(model);
    }
}
