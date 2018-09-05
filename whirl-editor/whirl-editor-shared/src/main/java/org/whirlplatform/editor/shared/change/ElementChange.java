package org.whirlplatform.editor.shared.change;

import org.whirlplatform.meta.shared.editor.AbstractElement;

public abstract class ElementChange<T extends AbstractElement> implements
        Change<T> {

    protected T changedElement;

    protected ElementChange(T changedElement) {
        this.changedElement = changedElement;
    }

    public T getChangedElement() {
        return changedElement;
    }

}
