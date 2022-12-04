package org.whirlplatform.editor.server.merge;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.whirlplatform.editor.shared.visitor.GraphVisitor;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.CellElement;
import org.whirlplatform.meta.shared.editor.CellRangeElement;
import org.whirlplatform.meta.shared.editor.ColumnElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.ContextMenuItemElement;
import org.whirlplatform.meta.shared.editor.ElementVisitor;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.meta.shared.editor.EventParameterElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.FormElement;
import org.whirlplatform.meta.shared.editor.GroupElement;
import org.whirlplatform.meta.shared.editor.ReportElement;
import org.whirlplatform.meta.shared.editor.RequestElement;
import org.whirlplatform.meta.shared.editor.RightCollectionElement;
import org.whirlplatform.meta.shared.editor.RowElement;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.DatabaseTableElement;
import org.whirlplatform.meta.shared.editor.db.DynamicTableElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.editor.db.ViewElement;

public class ChildCollectorVisitor
        extends GraphVisitor<ChildCollectorVisitor.ChildCollectorVisitorContext> {

    public Set<AbstractElement> collect(AbstractElement element) {
        ChildCollectorVisitorContext ctx = new ChildCollectorVisitorContext();
        element.accept(ctx, this);
        return ctx.getResult();
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, AbstractElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, ApplicationElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, CellElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, CellRangeElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, ColumnElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, ComponentElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, EventElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, EventParameterElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, FileElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, FormElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, GroupElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, ReportElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, RequestElement element) {
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, RightCollectionElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, RowElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, DataSourceElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, SchemaElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, TableColumnElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, AbstractTableElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, ViewElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, DatabaseTableElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, PlainTableElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, DynamicTableElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(ChildCollectorVisitorContext ctx, ContextMenuItemElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    class ChildCollectorVisitorContext implements ElementVisitor.VisitContext {
        private Set<AbstractElement> result = new HashSet<>();

        public ChildCollectorVisitorContext() {
        }

        public void addResult(AbstractElement element) {
            result.add(element);
        }

        public Set<AbstractElement> getResult() {
            return Collections.unmodifiableSet(result);
        }
    }

}
