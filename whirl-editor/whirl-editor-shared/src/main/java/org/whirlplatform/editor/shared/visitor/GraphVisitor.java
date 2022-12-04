package org.whirlplatform.editor.shared.visitor;

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

public class GraphVisitor<T extends ElementVisitor.VisitContext> implements ElementVisitor<T> {

    @Override
    public void visit(T ctx, AbstractElement element) {
        _visit(ctx, element);
    }

    private void _visit(T ctx, AbstractElement element) {

    }

    @Override
    public void visit(T ctx, ApplicationElement element) {

        // проверяем группы
        for (GroupElement e : element.getGroups()) {
            e.accept(ctx, this);
        }

        // проверяем корнеовй компонент
        if (element.getRootComponent() != null) {
            element.getRootComponent().accept(ctx, this);
        }

        // проверяем свободные копоненты
        for (ComponentElement e : element.getFreeComponents()) {
            e.accept(ctx, this);
        }

        // проверяем свободные события
        for (EventElement e : element.getFreeEvents()) {
            e.accept(ctx, this);
        }

        // проверяем источники данных
        for (DataSourceElement e : element.getDataSources()) {
            e.accept(ctx, this);
        }

    }

    @Override
    public void visit(T ctx, CellElement element) {

    }

    @Override
    public void visit(T ctx, CellRangeElement element) {

    }

    @Override
    public void visit(T ctx, ColumnElement element) {

    }

    @Override
    public void visit(T ctx, ComponentElement element) {
        _visit(ctx, element);
    }

    private void _visit(T ctx, ComponentElement element) {
        // проверяем события
        for (EventElement e : element.getEvents()) {
            e.accept(ctx, this);
        }

        // проверяем подчиненные компоненты
        for (ComponentElement e : element.getChildren()) {
            e.accept(ctx, this);
        }

        // Проверяем элементы контекстного меню
        for (ContextMenuItemElement e : element.getContextMenuItems()) {
            e.accept(ctx, this);
        }
    }

    @Override
    public void visit(T ctx, EventElement element) {

        // проверяем параметры события
        for (EventParameterElement e : element.getParameters()) {
            e.accept(ctx, this);
        }

        // проверяем подчиненные события
        for (EventElement e : element.getSubEvents()) {
            e.accept(ctx, this);
        }
    }

    @Override
    public void visit(T ctx, EventParameterElement element) {
    }

    @Override
    public void visit(T ctx, FileElement element) {

    }

    @Override
    public void visit(T ctx, FormElement element) {

        _visit(ctx, element);

        // проверяем ширину колонок
        for (ColumnElement e : element.getColumnsWidth()) {
            e.accept(ctx, this);
        }

        // проверяем высоту строк
        for (RowElement e : element.getRowsHeight()) {
            e.accept(ctx, this);
        }

        // проверяем ячейки
        for (CellElement e : element.getCells().values()) {
            e.accept(ctx, this);
        }

        // проверяем запросы
        for (RequestElement e : element.getRowRequests()) {
            e.accept(ctx, this);
        }
    }

    @Override
    public void visit(T ctx, GroupElement element) {

    }

    @Override
    public void visit(T ctx, ReportElement element) {

        // проверяем подчиненные компоненты
        for (ComponentElement e : element.getChildren()) {
            e.accept(ctx, this);
        }
    }

    @Override
    public void visit(T ctx, RequestElement element) {
    }

    @Override
    public void visit(T ctx, RightCollectionElement element) {

    }

    @Override
    public void visit(T ctx, RowElement element) {

    }

    @Override
    public void visit(T ctx, DataSourceElement element) {

        // проверяем все схемы
        for (SchemaElement e : element.getSchemas()) {
            e.accept(ctx, this);
        }

    }

    @Override
    public void visit(T ctx, SchemaElement element) {
        for (AbstractTableElement e : element.getTables()) {
            e.accept(ctx, this);
        }
    }

    @Override
    public void visit(T ctx, TableColumnElement element) {

    }

    @Override
    public void visit(T ctx, AbstractTableElement element) {

    }

    @Override
    public void visit(T ctx, ViewElement element) {

    }

    @Override
    public void visit(T ctx, DatabaseTableElement element) {

    }

    @Override
    public void visit(T ctx, PlainTableElement element) {
        for (TableColumnElement e : element.getColumns()) {
            e.accept(ctx, this);
        }
        if (element.getView() != null) {
            element.getView().accept(ctx, this);
        }
    }

    @Override
    public void visit(T ctx, DynamicTableElement element) {

    }

    @Override
    public void visit(T ctx, ContextMenuItemElement element) {
        for (EventElement e : element.getEvents()) {
            e.accept(ctx, this);
        }
        for (ContextMenuItemElement c : element.getChildren()) {
            c.accept(ctx, this);
        }
    }

}
