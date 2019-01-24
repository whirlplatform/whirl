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

        //TODO remove after fix
        PALETTE_EXCLUSIONS.add(ComponentType.TreeMenuType);
        PALETTE_EXCLUSIONS.add(ComponentType.TreeMenuItemType);
        PALETTE_EXCLUSIONS.add(ComponentType.HorizontalMenuType);
        PALETTE_EXCLUSIONS.add(ComponentType.HorizontalMenuItemType);
    }

    private ComponentPreferences() {
    }

}
