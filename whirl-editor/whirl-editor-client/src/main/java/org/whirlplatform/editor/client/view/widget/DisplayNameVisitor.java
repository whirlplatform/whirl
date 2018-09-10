package org.whirlplatform.editor.client.view.widget;

import org.whirlplatform.editor.client.tree.dummy.*;
import org.whirlplatform.editor.client.tree.visitor.TreeElementVisitor;
import org.whirlplatform.meta.shared.editor.*;
import org.whirlplatform.meta.shared.editor.db.*;

/**
 * Формирует отображаемые имена
 */
public class DisplayNameVisitor implements TreeElementVisitor<DisplayNameVisitContext> {
    // private EditorMessage message = EditorMessage.Util.MESSAGE;

    public DisplayNameVisitor() {
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, AbstractElement element) {
        ctx.setClassName("AbstractElement");
        ctx.setDisplayName(element.getName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, ApplicationElement element) {
        ctx.setClassName("Application");
        ctx.setDisplayName(element.getCode());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, CellElement element) {
        ctx.setClassName("Cell");
        ctx.setDisplayName(element.toString());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, CellRangeElement element) {
        ctx.setClassName("CellRange");
        ctx.setDisplayName(element.toString());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, ColumnElement element) {
        ctx.setClassName("Column");
        ctx.setDisplayName(element.getName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, ComponentElement element) {
        ctx.setClassName("Component");
        ctx.setDisplayName(element.getName(), element.getType().toString());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, EventElement element) {
        ctx.setClassName("Event");
        ctx.setDisplayName(element.getName(), element.getType().toString());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, EventParameterElement element) {
        ctx.setClassName("EventParameter");
        ctx.setDisplayName(element.getName(), element.getType().toString());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, FileElement element) {
        ctx.setClassName("File");
        ctx.setDisplayName(element.getFileName(), element.getContentType());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, FormElement element) {
        ctx.setClassName("Form");
        ctx.setDisplayName(element.getName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, GroupElement element) {
        ctx.setClassName("Group");
        ctx.setDisplayName(element.getGroupName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, ReportElement element) {
        ctx.setClassName("Report");
        ctx.setDisplayName(element.getName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, RequestElement element) {
        ctx.setClassName("Request");
        ctx.setDisplayName(element.getName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, RightCollectionElement element) {
        ctx.setClassName("RightsCollection");
        ctx.setDisplayName(element.getName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, RowElement element) {
        ctx.setClassName("Row");
        ctx.setDisplayName(element.getName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DataSourceElement element) {
        ctx.setClassName("DataSource");
        ctx.setDisplayName(element.getName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, SchemaElement element) {
        ctx.setClassName("Schema");
        ctx.setDisplayName(element.getSchemaName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, TableColumnElement element) {
        ctx.setClassName("TableColumn");
        ctx.setDisplayName(element.getColumnName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, AbstractTableElement element) {
        ctx.setClassName("AbstractTable");
        ctx.setDisplayName(element.getName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, ViewElement element) {
        ctx.setClassName("View");
        ctx.setDisplayName(element.getViewName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DatabaseTableElement element) {
        ctx.setClassName("DatabaseTable");
        ctx.setDisplayName(convertToString(element.getTitle()));
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, PlainTableElement element) {
        ctx.setClassName("PlainTable");
        ctx.setDisplayName(element.getTableName());
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DynamicTableElement element) {
        ctx.setClassName("DynamicTable");
        ctx.setDisplayName(convertToString(element.getTitle()));
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, ContextMenuItemElement element) {
        ctx.setClassName("ContextMenuItem");
        ctx.setDisplayName(convertToString(element.getLabel()));
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, AbstractDummyElement element) {
        ctx.setClassName("AbstractDummy");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyAppLocales element) {
        ctx.setClassName("Locales");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyAppComponents element) {
        ctx.setClassName("Components");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyAppFreeComponents element) {
        ctx.setClassName("FreeComponents");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyAppEvents element) {
        ctx.setClassName("AppEvents");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyAppDataSources element) {
        ctx.setClassName("AppDataSources");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyAppGroups element) {
        ctx.setClassName("AppGroups");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyAppReferences element) {
        ctx.setClassName("AppReferences");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyComponentEvents element) {
        ctx.setClassName("ComponentEvents");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyMenuItems element) {
        ctx.setClassName("MenuItems");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyMenuItemEvents element) {
        ctx.setClassName("MenuItemEvents");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummySchemas element) {
        ctx.setClassName("Schemas");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyEventParameters element) {
        ctx.setClassName("EventParameters");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyEventSubEvents element) {
        ctx.setClassName("EventSubEvents");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyPlainTables element) {
        ctx.setClassName("PlainTables");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyDynamicTables element) {
        ctx.setClassName("DynamicTables");
    }

    @Override
    public void visit(DisplayNameVisitContext ctx, DummyTableClones element) {
        ctx.setClassName("TableClones");
    }

    private String convertToString(final PropertyValue property) {
        return property.getDefaultValue().toString();
    }

}
