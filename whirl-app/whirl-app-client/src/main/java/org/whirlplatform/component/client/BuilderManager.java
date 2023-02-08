package org.whirlplatform.component.client;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.whirlplatform.component.client.window.WindowBuilder;
import org.whirlplatform.component.client.window.WindowManager;
import org.whirlplatform.meta.shared.ApplicationData;
import org.whirlplatform.meta.shared.component.ComponentType;

public class BuilderManager {

    private static Containable rootContainer;
    private static ApplicationData applicationData;

    public static Containable getRoot() {
        return rootContainer;
    }

    public static void setRoot(Containable container) {
        rootContainer = container;
    }

    public static Collection<ComponentBuilder> getAllBuilders() {
        Set<ComponentBuilder> result = new HashSet<ComponentBuilder>();
        result.addAll(allChildren(getRoot()));
        for (WindowBuilder w : WindowManager.get().getBuilders()) {
            result.add(w);
            result.addAll(allChildren(w));
        }
        return result;
    }

    public static Collection<ComponentBuilder> allChildren(
        Containable container) {
        Set<ComponentBuilder> result = new HashSet<ComponentBuilder>();
        for (ComponentBuilder b : container.getChildren()) {
            result.add(b);

            if (b instanceof Containable) {
                result.addAll(allChildren((Containable) b));
            }
        }
        return result;
    }

    public static ComponentBuilder getParentComponent(ComponentBuilder builder,
                                                      ComponentType type) {
        return getParentComponent(getRoot(), builder, type);
    }

    private static ComponentBuilder getParentComponent(Containable container,
                                                       ComponentBuilder builder,
                                                       ComponentType type) {
        ComponentBuilder result = null;

        for (ComponentBuilder child : container.getChildren()) {
            if (child.getId() == builder.getId()) {
                return null;
            }
            if (child instanceof Containable) {
                result = getParentComponent((Containable) child, builder, type);
                if (result == null) {
                    if (child.getType() == type || type == null) {
                        result = child;
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        return result;
    }

    public static ComponentBuilder findBuilder(String componentIdOrCode,
                                               boolean code) {
        if (componentIdOrCode == null) {
            return null;
        }
        ComponentBuilder result = null;
        if (BuilderManager.getRoot() != null) {
            result = findBuilder(BuilderManager.getRoot(), componentIdOrCode,
                code);
            if (result != null) {
                return result;
            }
        }

        for (WindowBuilder w : WindowManager.get().getBuilders()) {
            if ((!code && componentIdOrCode.equals(w.getId()))
                || (code && componentIdOrCode.equals(w.getCode()))) {
                return w;
            }

            result = findBuilder(w, componentIdOrCode, code);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    public static ComponentBuilder findBuilder(Containable containable,
                                               String componentIdOrCode, boolean code) {
        ComponentBuilder result;
        for (ComponentBuilder b : containable.getChildren()) {
            if ((!code && componentIdOrCode.equals(b.getId()))
                || (code && componentIdOrCode.equals(b.getCode()))) {
                return b;
            }

            if (b instanceof Containable) {
                result = findBuilder((Containable) b, componentIdOrCode, code);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    public static ApplicationData getApplicationData() {
        return applicationData;
    }

    public static void setApplicationData(ApplicationData data) {
        applicationData = data;
    }
}
