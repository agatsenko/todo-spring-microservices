/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.common.service.locator;

import java.util.List;

public interface ServiceLocator {
    <T> T get(Class<T> serviceClass);

    <T> T get(Class<T> serviceClass, Object serviceKey);

    <T> List<T> getAll(Class<T> serviceClass);
}
