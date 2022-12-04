package org.whirlplatform.editor.client.main;

import java.util.ArrayList;
import java.util.List;
import org.whirlplatform.meta.shared.component.ComponentType;

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
