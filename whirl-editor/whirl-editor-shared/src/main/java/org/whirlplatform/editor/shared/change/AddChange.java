package org.whirlplatform.editor.shared.change;

import org.whirlplatform.meta.shared.editor.AbstractElement;

public class AddChange<P extends AbstractElement, T extends AbstractElement>
    extends ElementChange<T> {

    protected P parentElement;

    public AddChange(P parentElement, T addedElement) {
        super(addedElement);
        this.parentElement = parentElement;
    }

    public P getParentElement() {
        return parentElement;
    }

}
