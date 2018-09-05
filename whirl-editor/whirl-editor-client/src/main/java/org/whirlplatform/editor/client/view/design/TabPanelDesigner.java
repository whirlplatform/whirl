package org.whirlplatform.editor.client.view.design;

import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.widget.core.client.TabPanel;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.editor.client.dnd.TabPanelDropTarget;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class TabPanelDesigner extends ComponentDesigner {

    public TabPanelDesigner(LocaleElement defaultLocale, ComponentElement element) {
        super(defaultLocale, element);
    }

    protected ComponentBuilder initChildComponent(ComponentElement element) {
        ComponentBuilder child = super.initChildComponent(element);
        bindRootComponent(element, child);
        return child;
    }

    @Override
    protected void initRootDropTarget(final ComponentElement element, final ComponentBuilder builder) {
        new TabPanelDropTarget((TabPanel) builder.getComponent()) {
            @Override
            protected void onDragDrop(DndDropEvent event) {
                TabPanelDesigner.super.onRootDrop(element, builder, event.getData(), getIndex());
                super.onDragDrop(event);
            }
        };
    }

    @Override
    protected void onRootDropSetLocationData(ComponentElement rootElement, ComponentElement element,
                                             Object locationData) {
        int index = locationData == null ? -1 : (Integer) locationData;
        if (index >= 0) {
            for (ComponentElement e : rootElement.getChildren()) {
                Double idx = e.getProperty(PropertyType.LayoutDataIndex).getValue(null).getDouble();
                if (idx == null) {
                    continue;
                }
                int i = idx.intValue();
                if (i >= index) {
                    fireComponentPropertyChangeEvent(new ComponentPropertyChangeEvent(e, PropertyType.LayoutDataIndex,
                            new PropertyValue(DataType.NUMBER, defaultLocale, index)));
                }
            }
            fireComponentPropertyChangeEvent(new ComponentPropertyChangeEvent(element, PropertyType.LayoutDataIndex,
                    new PropertyValue(DataType.NUMBER, defaultLocale, index)));
        }
    }

    @Override
    protected void onRootDropAddChild(ComponentElement parentElement, ComponentElement element) {
        if (element.getType() == ComponentType.TabItemType) {
            return;
        }
        super.onRootDropAddChild(parentElement, element);
    }
}
