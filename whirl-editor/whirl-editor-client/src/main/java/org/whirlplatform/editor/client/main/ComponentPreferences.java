package org.whirlplatform.editor.client.main;

import org.whirlplatform.meta.shared.component.ComponentType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Pavel Semenov
 */
public class ComponentPreferences {

    public static final List<ComponentType> PALETTE_EXCLUSIONS = new ArrayList<>();

    static {
        PALETTE_EXCLUSIONS.add(ComponentType.ContextMenuItemType);
    }

    private ComponentPreferences() {
    }

}
