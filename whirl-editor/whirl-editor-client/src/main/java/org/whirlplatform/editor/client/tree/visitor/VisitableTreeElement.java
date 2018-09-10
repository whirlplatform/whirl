package org.whirlplatform.editor.client.tree.visitor;

import org.whirlplatform.meta.shared.editor.ElementVisitable;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

/**
 * Provides visitable interface for the elements which were not defined
 * in ElementVisitor class
 */
public interface VisitableTreeElement extends ElementVisitable {
    <T extends VisitContext> void accept(T ctx, TreeElementVisitor<T> visitor);
}
