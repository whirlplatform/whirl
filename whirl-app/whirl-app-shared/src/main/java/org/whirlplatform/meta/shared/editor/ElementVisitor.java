package org.whirlplatform.meta.shared.editor;

import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;
import org.whirlplatform.meta.shared.editor.db.*;

public interface ElementVisitor<T extends VisitContext> {

    interface VisitContext {

    }

    // component
    void visit(T ctx, AbstractElement element);

    void visit(T ctx, ApplicationElement element);

    void visit(T ctx, CellElement element);

    void visit(T ctx, CellRangeElement element);

    void visit(T ctx, ColumnElement element);

    void visit(T ctx, ComponentElement element);

    void visit(T ctx, EventElement element);

    void visit(T ctx, EventParameterElement element);

    void visit(T ctx, FileElement element);

    void visit(T ctx, FormElement element);

    void visit(T ctx, GroupElement element);

    void visit(T ctx, ReportElement element);

    void visit(T ctx, RequestElement element);

    void visit(T ctx, RightCollectionElement element);

    void visit(T ctx, RowElement element);

    // datasource
    void visit(T ctx, DataSourceElement element);

    void visit(T ctx, SchemaElement element);

    void visit(T ctx, TableColumnElement element);

    void visit(T ctx, AbstractTableElement element);

    void visit(T ctx, ViewElement element);

    void visit(T ctx, DatabaseTableElement element);

    void visit(T ctx, PlainTableElement element);

    void visit(T ctx, DynamicTableElement element);

    void visit(T ctx, ContextMenuItemElement element);

}
