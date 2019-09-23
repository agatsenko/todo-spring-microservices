/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.common.service.locator.spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.agatsenko.todo.service.common.service.locator.ServiceLocator;
import io.agatsenko.todo.service.common.service.locator.ServiceLocatorException;

@RequiredArgsConstructor
public class SpringServiceLocator implements ServiceLocator {
    @NonNull
    private final ApplicationContext context;

    @Override
    public <T> T get(@NonNull Class<T> serviceClass) {
        try {
            return context.getBean(serviceClass);
        }
        catch (RuntimeException ex) {
            throw new ServiceLocatorException(String.format("unable to get %s service", serviceClass.getName()), ex);
        }
    }

    @Override
    public <T> T get(@NonNull Class<T> serviceClass, @NonNull Object serviceKey) {
        try {
            return context.getBean(serviceKey.toString(), serviceClass);
        }
        catch (RuntimeException ex) {
            throw new ServiceLocatorException(
                    String.format("unable to get %s service by '%s' key", serviceClass.getName(), serviceKey),
                    ex
            );
        }
    }

    @Override
    public <T> List<T> getAll(Class<T> serviceClass) {
        try {
            return new ArrayList<>(context.getBeansOfType(serviceClass).values());
        }
        catch (RuntimeException ex) {
            throw new ServiceLocatorException(
                    String.format("unable to get all %s services", serviceClass.getName()),
                    ex
            );
        }
    }
}
