package org.whirlplatform.component.client.event;

import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.component.ComponentModel;

public interface EventManager {

    class Util {

        private static EventManager eventManager;

        public static void set(EventManager manager) {
            eventManager = manager;
        }

        public static EventManager get() {
            return eventManager;
        }
    }

    EventHelper wrapEvent(EventMetadata metadata);

    void addEvents(ComponentBuilder builder, ComponentModel model);

    void addMenuTreeEvent(ComponentBuilder builder, EventMetadata e);

}
