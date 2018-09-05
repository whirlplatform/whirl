package org.whirlplatform.editor.client.meta;

import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

@SuppressWarnings("serial")
public class NewComponentElement extends NewElement {

    private ComponentType type;

    public NewComponentElement() {
        super();
    }

    public NewComponentElement(ComponentType type) {
        this.type = type;
    }

    public ComponentType getType() {
        return type;
    }

    @Override
    public void accept(VisitContext ctx, ElementVisitor visitor) {

    }
}
