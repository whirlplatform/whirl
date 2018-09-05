package org.whirlplatform.editor.shared.change;

import org.whirlplatform.meta.shared.editor.AbstractElement;

public class ModifyChange<T extends AbstractElement> extends ElementChange<T> {

    protected T oldElement;

    protected ModifyChange(T oldElement, T changedElement) {
        super(changedElement);
        this.oldElement = oldElement;
    }

    public T getOldElement() {
        return oldElement;
    }

}
