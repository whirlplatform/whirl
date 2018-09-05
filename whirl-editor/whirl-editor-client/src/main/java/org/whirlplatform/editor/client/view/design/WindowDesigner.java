package org.whirlplatform.editor.client.view.design;

import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;

public class WindowDesigner extends ComponentDesigner {

    public WindowDesigner(LocaleElement defaultLocale, ComponentElement element) {
        super(defaultLocale, element);
    }

    @Override
    protected ComponentType componentType(ComponentElement element) {
        return ComponentType.SimpleContainerType;
    }
}
