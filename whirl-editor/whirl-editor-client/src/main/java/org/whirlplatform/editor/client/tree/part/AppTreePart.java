package org.whirlplatform.editor.client.tree.part;

import org.whirlplatform.editor.client.tree.AppTreeBase;
import org.whirlplatform.meta.shared.editor.AbstractElement;

/**
 *
 */
public interface AppTreePart<T extends AbstractElement> extends AppTreeBase {

    void init();

    T getHandledElement();

    void clear();
}
