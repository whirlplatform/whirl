package org.whirlplatform.component.client.js;

import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.BuilderManager;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;

/**
 * This helper class contains static methods to work with application components.
 */
@JsType
public class Components {

    /**
     * Finds component by code.
     *
     * @param code component code
     * @return component
     */
    public static ComponentBuilder findByCode(String code) {
        return BuilderManager.findBuilder(code, true);
    }

    /**
     * Finds component by code in the particular container.
     *
     * @param container
     * @param code      component code
     * @return component
     */
    public static ComponentBuilder findByCodeInContainer(
            ComponentBuilder container, String code) {
        if (container instanceof Containable) {
            return BuilderManager
                    .findBuilder((Containable) container, code, true);
        } else {
            return null;
        }
    }

    /**
     * Returns all application components currently available in application.
     *
     * @return components array
     */
    public static ComponentBuilder[] getAll() {
        return BuilderManager.getAllBuilders().toArray(new ComponentBuilder[0]);
    }

}