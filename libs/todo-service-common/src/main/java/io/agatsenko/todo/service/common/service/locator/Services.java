/**
 * Author: Alexander Gatsenko (alexandr.gatsenko@gmail.com)
 * Created: 2019-09-21
 */
package io.agatsenko.todo.service.common.service.locator;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Services {
    private static volatile ServiceLocator locator;

    public static ServiceLocator locator() {
        var locator = Services.locator;
        if (locator == null) {
            throw new ServiceLocatorException("locator is not defined");
        }
        return locator;
    }

    public static void setLocator(ServiceLocator locator) {
        Services.locator = locator;
    }
}
