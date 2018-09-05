package org.whirlplatform.editor.client.meta;

import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

@SuppressWarnings("serial")
public class NullRootComponentElement extends NewElement {

    public NullRootComponentElement() {
        super();
    }

    @Override
    public void accept(VisitContext ctx, ElementVisitor visitor) {

    }

}
