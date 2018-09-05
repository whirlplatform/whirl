package org.whirlplatform.editor.client.meta;

import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

@SuppressWarnings("serial")
public class NewContextMenuItemElement extends NewElement {

    public NewContextMenuItemElement() {
        super();
    }

    @Override
    public <T extends VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {

    }
}
