package org.whirlplatform.editor.client.tree.visitor;

import org.whirlplatform.editor.client.tree.dummy.*;
import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

public interface TreeElementVisitor<T extends VisitContext> extends ElementVisitor<T> {

	void visit(T ctx, AbstractDummyElement element);

	void visit(T ctx, DummyAppLocales element);

	void visit(T ctx, DummyAppComponents element);

	void visit(T ctx, DummyAppFreeComponents element);

	void visit(T ctx, DummyAppEvents element);

	void visit(T ctx, DummyAppDataSources element);

	void visit(T ctx, DummyAppGroups element);

	void visit(T ctx, DummyAppReferences element);

	void visit(T ctx, DummyComponentEvents element);

	void visit(T ctx, DummyMenuItems element);

	void visit(T ctx, DummyMenuItemEvents element);

	void visit(T ctx, DummySchemas element);

	void visit(T ctx, DummyEventParameters element);

	void visit(T ctx, DummyEventSubEvents element);

	void visit(T ctx, DummyPlainTables element);

	void visit(T ctx, DummyDynamicTables element);

	void visit(T ctx, DummyTableClones element);
}
