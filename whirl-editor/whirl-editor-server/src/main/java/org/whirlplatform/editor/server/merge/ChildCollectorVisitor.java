package org.whirlplatform.editor.server.merge;

import org.whirlplatform.editor.shared.visitor.GraphVisitor;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChildCollectorVisitor extends GraphVisitor<ChildCollectorVisitor.ChildCollectorVisitorContext> {

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


}
