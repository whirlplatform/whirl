package org.whirlplatform.editor.server;

import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.component.ComponentProperties;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.CellElement;
import org.whirlplatform.meta.shared.editor.ColumnElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.meta.shared.editor.EventParameterElement;
import org.whirlplatform.meta.shared.editor.FileElement;
import org.whirlplatform.meta.shared.editor.FormElement;
import org.whirlplatform.meta.shared.editor.GroupElement;
import org.whirlplatform.meta.shared.editor.PropertyValue;
import org.whirlplatform.meta.shared.editor.ReportElement;
import org.whirlplatform.meta.shared.editor.RequestElement;
import org.whirlplatform.meta.shared.editor.RightCollectionElement;
import org.whirlplatform.meta.shared.editor.RowElement;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.SchemaElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.meta.shared.editor.db.ViewElement;

public class ApplicationIdHelper {

    /**
     * Подмена временных ID на постоянные должна исчезнуть с введенеим {@link RandomUUID}
     *
     * @param application
     */
    @Deprecated
    public static void initIdsOf(ApplicationElement application) {
        // Id приложения
        if (isTempId(application)) {
            application.setId(getNextId());
        }

        // id событий
        for (EventElement event : application.getFreeEvents()) {
            initEventId(event);
        }

        // id компонентов
        initComponentId(application.getRootComponent());
        for (ComponentElement component : application.getFreeComponents()) {
            initComponentId(component);
        }

        // файлы javascript
        for (FileElement file : application.getJavaScriptFiles()) {
            initFileId(file);
        }

        // файлы css
        for (FileElement file : application.getCssFiles()) {
            initFileId(file);
        }

        // файлы java
        for (FileElement file : application.getJavaFiles()) {
            initFileId(file);
        }

        // файлы image
        for (FileElement file : application.getImageFiles()) {
            initFileId(file);
        }

        // файл static
        initFileId(application.getStaticFile());

        for (DataSourceElement ds : application.getDataSources()) {
            initDataSourceId(application, ds);
        }

        // группы
        for (GroupElement g : application.getGroups()) {
            initGroupId(g);
        }

        // права таблиц
        for (RightCollectionElement c : application.getAllTableRights()) {
            initRightCollectionId(c);
        }

        // права колонок таблиц
        for (RightCollectionElement c : application.getAllTableColumnRights()) {
            initRightCollectionId(c);
        }

        // права событий
        for (RightCollectionElement c : application.getAllEventRights()) {
            initRightCollectionId(c);
        }

        // пройдем по всем свойствам компонентов и проставим правильные
        // идентификаторы

    }

    @Deprecated
    private static boolean isTempId(AbstractElement element) {
        return element.getId() == null || element.getId().startsWith("temp");
    }

    @Deprecated
    private static void initRightCollectionId(RightCollectionElement rightCollection) {
        if (isTempId(rightCollection)) {
            rightCollection.setId(getNextId());
        }
    }

    @Deprecated
    private static void initGroupId(GroupElement group) {
        if (isTempId(group)) {
            group.setId(getNextId());
        }
    }

    @Deprecated
    private static void initDataSourceId(ApplicationElement application,
                                         DataSourceElement datasource) {
        if (datasource == null) {
            return;
        }
        if (isTempId(datasource)) {
            datasource.setId(getNextId());
        }
        for (SchemaElement s : datasource.getSchemas()) {
            initSchemaId(application, s);
        }
    }

    @Deprecated
    private static void initSchemaId(ApplicationElement application, SchemaElement schema) {
        if (schema == null) {
            return;
        }
        if (isTempId(schema)) {
            schema.setId(getNextId());
        }
        for (AbstractTableElement t : schema.getTables()) {
            initTableId(application, t);
            if (t instanceof PlainTableElement) {
                for (AbstractTableElement clone : ((PlainTableElement) t).getClones()) {
                    initTableId(application, clone);
                }
            }
        }
    }

    @Deprecated
    private static void refreshTableId(ApplicationElement application, String tempTableId,
                                       String tableId) {
        // обновляем ссылки на таблицы в компонентах
        for (ComponentElement c : application.getAvailableComponents()) {
            if (ComponentProperties.getProperties(c.getType()).contains(PropertyType.DataSource)) {
                PropertyValue v = c.getProperty(PropertyType.DataSource);
                if (v != null && v.getValue(v.getDefaultLocale()) != null) {
                    DataValue d = v.getValue(v.getDefaultLocale());
                    if (d.getType() == DataType.LIST
                            && tempTableId.equals(d.getListModelData().getId())) {
                        d.getListModelData().setId(tableId);
                    }
                }
            }
        }

        // TODO Обновляем ссылки на таблицы в событиях
    }

    @Deprecated
    private static void initTableId(ApplicationElement application, AbstractTableElement table) {
        if (table == null) {
            return;
        }
        if (isTempId(table)) {
            String tempTableId = table.getId();
            table.setId(getNextId());
            refreshTableId(application, tempTableId, table.getId());
        }

        if (table instanceof PlainTableElement) {
            PlainTableElement tableEl = (PlainTableElement) table;
            initViewId(tableEl.getView());
            for (TableColumnElement c : tableEl.getColumns()) {
                initColumnId(c);
            }
        }
    }

    @Deprecated
    private static void initViewId(ViewElement view) {
        if (view == null) {
            return;
        }
        if (isTempId(view)) {
            view.setId(getNextId());
        }
    }

    @Deprecated
    private static void initColumnId(TableColumnElement column) {
        if (column == null) {
            return;
        }
        if (isTempId(column)) {
            column.setId(getNextId());
        }
    }

    @Deprecated
    private static void initFileId(FileElement file) {
        if (file == null) {
            return;
        }
        if (isTempId(file)) {
            file.setId(getNextId());
        }
    }

    @Deprecated
    private static void initComponentId(ComponentElement component) {
        if (component == null) {
            return;
        }
        if (isTempId(component)) {
            component.setId(getNextId());
        }

        // id событий
        for (EventElement event : component.getEvents()) {
            initEventId(event);
        }

        for (ComponentElement c : component.getChildren()) {
            initComponentId(c);
        }

        if (component instanceof FormElement) {
            initFormId((FormElement) component);
        }

        if (component instanceof ReportElement) {
            initReportId((ReportElement) component);
        }
    }

    @Deprecated
    private static void initFormId(FormElement form) {
        if (form == null) {
            return;
        }
        // ячейки
        for (CellElement cell : form.getCells().values()) {
            if (isTempId(cell)) {
                cell.setId(getNextId());
            }
        }

        // ширина колонок
        for (ColumnElement e : form.getColumnsWidth()) {
            if (isTempId(e)) {
                e.setId(getNextId());
            }
        }

        // высота строк
        for (RowElement e : form.getRowsHeight()) {
            if (isTempId(e)) {
                e.setId(getNextId());
            }
        }

        // запросы
        for (RequestElement request : form.getRowRequests()) {
            if (isTempId(request)) {
                request.setId(getNextId());
            }
        }
    }

    @Deprecated
    private static void initEventId(EventElement event) {
        if (event == null) {
            return;
        }
        if (isTempId(event)) {
            event.setId(getNextId());
        }

        for (EventParameterElement p : event.getParameters()) {
            if (isTempId(p)) {
                p.setId(getNextId());
            }
        }
    }

    @Deprecated
    private static void initReportId(ReportElement report) {
        if (report == null) {
            return;
        }
        for (FieldMetadata f : report.getFields()) {
            if (f.getId() == null || f.getId().startsWith("temp")) {
                f.setId(getNextId());
            }
        }
    }

    private static String getNextId() {
        return RandomUUID.uuid();
    }
}
