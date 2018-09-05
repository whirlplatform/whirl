package org.whirlplatform.editor.shared.change;

import org.whirlplatform.meta.shared.editor.AbstractElement;

public class RemoveChange<T extends AbstractElement> extends ElementChange<T> {

    protected T parentElement;

    public RemoveChange(T parentElement, T changedElement) {
        super(changedElement);
        this.parentElement = parentElement;
    }

    public T getParentElement() {
        return parentElement;
    }

}
