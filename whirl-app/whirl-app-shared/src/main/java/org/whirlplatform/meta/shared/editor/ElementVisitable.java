package org.whirlplatform.meta.shared.editor;

import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

public interface ElementVisitable {

    <T extends VisitContext> void accept(T ctx, ElementVisitor<T> visitor);

}
