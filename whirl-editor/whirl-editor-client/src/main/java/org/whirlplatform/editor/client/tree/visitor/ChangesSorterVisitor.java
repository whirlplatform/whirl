package org.whirlplatform.editor.client.tree.visitor;

import org.whirlplatform.editor.client.tree.dummy.*;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.*;

/**
 *
 */
public class ChangesSorterVisitor implements TreeElementVisitor<ChangesSorterVisitContext> {

    public ChangesSorterVisitor() {
    }

    private void setAsSelfOwned(ChangesSorterVisitContext ctx, final String ownerId) {
        ctx.setChangeOwnerId(ownerId);
    }

    private void setAsRootOwned(ChangesSorterVisitContext ctx) {
        ctx.setChangeOwnerId(ctx.getRootId());
    }

    private void setAsDummy(ChangesSorterVisitContext ctx, AbstractElement element) {
        ctx.setChangeOwnerId(null);
        StringBuilder sb = new StringBuilder("*** Warning! *** Detected changes for dummy class: ");
        sb.append(element.getClass().getSimpleName());
        System.out.println(sb.toString());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, AbstractElement element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, ApplicationElement element) {
        if (element.getId().equals(ctx.getRootId())) {
            setAsSelfOwned(ctx, element.getId());
        } else {
            setAsRootOwned(ctx);
        }
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, CellElement element) {
        ctx.setChangeOwnerId(ctx.getParentId(element));
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, CellRangeElement element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, ColumnElement element) {
        ctx.setChangeOwnerId(ctx.getParentId(element));
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, ComponentElement element) {
        setAsSelfOwned(ctx, element.getId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, EventElement element) {
        setAsSelfOwned(ctx, element.getId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, EventParameterElement element) {
        setAsSelfOwned(ctx, element.getId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, FileElement element) {
        setAsRootOwned(ctx);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, FormElement element) {
        setAsSelfOwned(ctx, element.getId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, GroupElement element) {
        ctx.setChangeOwnerId(ctx.getDummyGroupsId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, ReportElement element) {
        setAsSelfOwned(ctx, element.getId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, RequestElement element) {
        ctx.setChangeOwnerId(ctx.getParentId(element));
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, RightCollectionElement element) {
        ctx.setChangeOwnerId(ctx.getParentId(element));
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, RowElement element) {
        ctx.setChangeOwnerId(ctx.getParentId(element));
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DataSourceElement element) {
        setAsSelfOwned(ctx, element.getId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, SchemaElement element) {
        setAsSelfOwned(ctx, element.getId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, TableColumnElement element) {
        ctx.setChangeOwnerId(ctx.getParentId(element));
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, AbstractTableElement element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, ViewElement element) {
        ctx.setChangeOwnerId(ctx.getParentId(element));
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DatabaseTableElement element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, PlainTableElement element) {
        setAsSelfOwned(ctx, element.getId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DynamicTableElement element) {
        setAsSelfOwned(ctx, element.getId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, ContextMenuItemElement element) {
        setAsSelfOwned(ctx, element.getId());
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, AbstractDummyElement element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyAppLocales element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyAppComponents element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyAppFreeComponents element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyAppEvents element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyAppDataSources element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyAppGroups element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyAppReferences element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyComponentEvents element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyMenuItems element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyMenuItemEvents element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummySchemas element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyEventParameters element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyEventSubEvents element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyPlainTables element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyDynamicTables element) {
        setAsDummy(ctx, element);
    }

    @Override
    public void visit(ChangesSorterVisitContext ctx, DummyTableClones element) {
        setAsDummy(ctx, element);
    }

}
