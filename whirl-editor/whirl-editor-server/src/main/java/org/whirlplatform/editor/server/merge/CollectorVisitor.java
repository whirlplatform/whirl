package org.whirlplatform.editor.server.merge;

import org.whirlplatform.editor.shared.visitor.GraphVisitor;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class CollectorVisitor extends GraphVisitor<CollectorVisitor.CollectorVisitorContext> {

    class CollectorVisitorContext implements ElementVisitor.VisitContext {
        private Set<AbstractElement> result = new HashSet<>();

        public CollectorVisitorContext() {
        }

        public void addResult(AbstractElement element) {
            result.add(element);
        }

        public Set<AbstractElement> getResult() {
            return Collections.unmodifiableSet(result);
        }
    }

    public Set<AbstractElement> collect(AbstractElement element) {
        CollectorVisitorContext ctx = new CollectorVisitorContext();
        element.accept(ctx, this);
        return ctx.getResult();
    }

    @Override
    public void visit(CollectorVisitorContext ctx, AbstractElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, ApplicationElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, CellElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, CellRangeElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, ColumnElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, ComponentElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, EventElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, EventParameterElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, FileElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, FormElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, GroupElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, ReportElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, RequestElement element) {
    }

    @Override
    public void visit(CollectorVisitorContext ctx, RightCollectionElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, RowElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, DataSourceElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, SchemaElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, TableColumnElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, AbstractTableElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, ViewElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, DatabaseTableElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, PlainTableElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, DynamicTableElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }

    @Override
    public void visit(CollectorVisitorContext ctx, ContextMenuItemElement element) {
        ctx.addResult(element);
        super.visit(ctx, element);
    }


}
