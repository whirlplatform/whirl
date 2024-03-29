package org.whirlplatform.editor.client.view.design;

import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.widget.core.client.menu.MenuBar;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.editor.client.dnd.HorizontalMenuDropTarget;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class HorizontalMenuDesigner extends IndexContainerDesigner {

    public HorizontalMenuDesigner(LocaleElement defaultLocale, ComponentElement element) {
        super(defaultLocale, element);
    }

    @Override
    protected void initRootDropTarget(final ComponentElement element,
                                      final ComponentBuilder builder) {
        new HorizontalMenuDropTarget((MenuBar) builder.getComponent()) {
            @Override
            protected void onDragDrop(DndDropEvent event) {
                HorizontalMenuDesigner.super.onRootDrop(element, builder, event.getData(),
                    getIndex());
                super.onDragDrop(event);
            }
        };
    }

    @Override
    protected void onRootDropSetLocationData(ComponentElement rootElement, ComponentElement element,
                                             Object locationData) {
        int index = locationData == null ? -1 : (Integer) locationData;
        if (index >= 0) {
            fireComponentPropertyChangeEvent(
                new ComponentPropertyChangeEvent(element, PropertyType.LayoutDataIndex,
                    new PropertyValue(DataType.NUMBER, defaultLocale, index)));
        }
    }

    @Override
    protected void onRootDropAddChild(ComponentElement parentElement, ComponentElement element) {
        if (element.getType() == ComponentType.HorizontalMenuItemType) {
            super.onRootDropAddChild(parentElement, element);
        }
    }
}
