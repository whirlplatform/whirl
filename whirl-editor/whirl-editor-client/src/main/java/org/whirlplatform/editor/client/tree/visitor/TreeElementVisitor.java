package org.whirlplatform.editor.client.tree.visitor;

import org.whirlplatform.editor.client.tree.dummy.AbstractDummyElement;
import org.whirlplatform.editor.client.tree.dummy.DummyAppComponents;
import org.whirlplatform.editor.client.tree.dummy.DummyAppDataSources;
import org.whirlplatform.editor.client.tree.dummy.DummyAppEvents;
import org.whirlplatform.editor.client.tree.dummy.DummyAppFreeComponents;
import org.whirlplatform.editor.client.tree.dummy.DummyAppGroups;
import org.whirlplatform.editor.client.tree.dummy.DummyAppLocales;
import org.whirlplatform.editor.client.tree.dummy.DummyAppReferences;
import org.whirlplatform.editor.client.tree.dummy.DummyComponentEvents;
import org.whirlplatform.editor.client.tree.dummy.DummyDynamicTables;
import org.whirlplatform.editor.client.tree.dummy.DummyEventParameters;
import org.whirlplatform.editor.client.tree.dummy.DummyEventSubEvents;
import org.whirlplatform.editor.client.tree.dummy.DummyMenuItemEvents;
import org.whirlplatform.editor.client.tree.dummy.DummyMenuItems;
import org.whirlplatform.editor.client.tree.dummy.DummyPlainTables;
import org.whirlplatform.editor.client.tree.dummy.DummySchemas;
import org.whirlplatform.editor.client.tree.dummy.DummyTableClones;
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
