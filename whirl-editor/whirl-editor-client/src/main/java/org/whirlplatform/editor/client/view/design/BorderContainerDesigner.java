package org.whirlplatform.editor.client.view.design;

import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.editor.client.dnd.BorderLayoutDropTarget;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class BorderContainerDesigner extends ComponentDesigner {

    public BorderContainerDesigner(LocaleElement defaultLocale, ComponentElement element) {
        super(defaultLocale, element);
    }

    @Override
    protected void initRootDropTarget(final ComponentElement element,
                                      final ComponentBuilder builder) {
        new BorderLayoutDropTarget((BorderLayoutContainer) builder.getComponent()) {
            protected void onDragDrop(DndDropEvent event) {
                BorderContainerDesigner.super.onRootDrop(element, builder, event.getData(),
                        getLocation());
                super.onDragDrop(event);
            }

        };
    }

    @Override
    protected void onRootDropSetLocationData(ComponentElement rootElement, ComponentElement element,
                                             Object locationData) {
        fireComponentPropertyChangeEvent(
                new ComponentPropertyChangeEvent(element, PropertyType.LayoutDataLocation,
                        new PropertyValue(DataType.STRING, defaultLocale, locationData)));
    }
}
