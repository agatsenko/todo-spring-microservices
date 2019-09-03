/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-08-30
 */
package io.agatsenko.todo.service.common.web.api.dto;

import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.lang3.reflect.Typed;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import io.agatsenko.todo.util.Check;

public class DtoAssemblers implements DtoAssemblerProvider {
    private final Map<AssemblerKey, DtoAssembler<?, ?>> assemblers;

    public DtoAssemblers(Collection<DtoAssembler<?, ?>> assemblers) {
        Check.argNotNull(assemblers, "assemblers");
        this.assemblers = Collections.unmodifiableMap(
                assemblers.stream()
                        .filter(Objects::nonNull)
                        .peek(a -> {
                            if (a instanceof DtoAssemblerProviderAware) {
                                ((DtoAssemblerProviderAware) a).setDtoAssemblerProvider(this);
                            }
                        })
                        .collect(Collectors.toMap(a -> new AssemblerKey(a.getModelType(), a.getDtoType()), a -> a))
        );
    }

    public Collection<DtoAssembler<?, ?>> getAssemblers() {
        return assemblers.values();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <TModel, TDto> DtoAssembler<TModel, TDto> ensureGetAssembler(Type modelType, Type dtoType) {
        Check.argNotNull(modelType, "modelType");
        Check.argNotNull(dtoType, "dtoType");
        final var assembler = assemblers.get(new AssemblerKey(modelType, dtoType));
        Check.state(
                assembler != null,
                "unable to get dto assembler for modelType = %s, dtoType = %s",
                modelType,
                dtoType
        );
        return (DtoAssembler<TModel, TDto>) assembler;
    }

    public <TModel, TDto> TDto assembly(Class<TModel> modelType, Class<TDto> dtoType, TModel model) {
        return internalAssembly(modelType, dtoType, model);
    }

    public <TModel, TDto> TDto assembly(Class<TDto> dtoType, TModel model) {
        return model == null ? null : internalAssembly(model.getClass(), dtoType, model);
    }

    public <TModel, TDto> TDto assembly(Typed<TModel> modelType, Typed<TDto> dtoType, TModel model) {
        return internalAssembly(modelType.getType(), dtoType.getType(), model);
    }

    public <TModel, TDto> TDto assembly(Typed<TDto> dtoType, TModel model) {
        return model == null ? null : internalAssembly(model.getClass(), dtoType.getType(), model);
    }

    public <TModel, TDto> TDto assembly(Class<TModel> modelType, Typed<TDto> dtoType, TModel model) {
        return internalAssembly(modelType, dtoType.getType(), model);
    }

    public <TModel, TDto> TDto assembly(Typed<TModel> modelType, Class<TDto> dtoType, TModel model) {
        return internalAssembly(modelType.getType(), dtoType, model);
    }

    public <TModel, TDto> List<TDto> assembly(
            Class<TModel> modelType,
            Class<TDto> dtoType,
            Collection<TModel> models) {
        return internalAssembly(modelType, dtoType, models);
    }

    public <TModel, TDto> List<TDto> assembly(
            Typed<TModel> modelType,
            Typed<TDto> dtoType,
            Collection<TModel> models) {
        return internalAssembly(modelType.getType(), dtoType.getType(), models);
    }

    public <TModel, TDto> List<TDto> assembly(
            Typed<TModel> modelType,
            Class<TDto> dtoType,
            Collection<TModel> models) {
        return internalAssembly(modelType.getType(), dtoType, models);
    }

    public <TModel, TDto> List<TDto> assembly(
            Class<TModel> modelType,
            Typed<TDto> dtoType,
            Collection<TModel> models) {
        return internalAssembly(modelType, dtoType.getType(), models);
    }

    private <TModel, TDto> TDto internalAssembly(Type modelType, Type dtoType, TModel model) {
        return this.<TModel, TDto>ensureGetAssembler(modelType, dtoType).assembly(model);
    }

    private <TModel, TDto> List<TDto> internalAssembly(
            Type modelType,
            Class dtoType,
            Collection<TModel> models) {
        final var assembler = this.<TModel, TDto>ensureGetAssembler(modelType, dtoType);
        return models == null ?
                Collections.emptyList() :
                models.stream().map(assembler::assembly).collect(Collectors.toList());
    }

    @AllArgsConstructor
    @EqualsAndHashCode
    @ToString
    private static class AssemblerKey {
        public final Type modelType;
        public final Type dtoType;
    }
}
