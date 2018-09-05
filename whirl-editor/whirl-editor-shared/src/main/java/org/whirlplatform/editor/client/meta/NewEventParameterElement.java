package org.whirlplatform.editor.client.meta;

import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

@SuppressWarnings("serial")
public class NewEventParameterElement extends NewElement {

    private String componentId;
    private int index = -1;

    public NewEventParameterElement() {
        super();
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public void accept(VisitContext ctx, ElementVisitor visitor) {

    }

}
