package org.whirlplatform.editor.client.view.design;

import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class IndexContainerDesigner extends ComponentDesigner {

    public IndexContainerDesigner(LocaleElement defaultLocale, ComponentElement element) {
        super(defaultLocale, element);
    }

    @Override
    protected void onRootDropSetLocationData(ComponentElement rootElement, ComponentElement element,
                                             Object locationData) {
        int index = (locationData == null) ? -1 : (Integer) locationData;
        if (index >= 0) {
            fireComponentPropertyChangeEvent(
                    new ComponentPropertyChangeEvent(element, PropertyType.LayoutDataIndex,
                            new PropertyValue(DataType.NUMBER, defaultLocale, index)));
        }
    }
}
