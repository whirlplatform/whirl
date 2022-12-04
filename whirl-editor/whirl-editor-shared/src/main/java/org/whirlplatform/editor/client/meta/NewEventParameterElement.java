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

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void accept(VisitContext ctx, ElementVisitor visitor) {

    }

}
