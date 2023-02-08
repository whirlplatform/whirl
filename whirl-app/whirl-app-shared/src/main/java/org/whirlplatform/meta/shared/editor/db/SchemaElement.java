package org.whirlplatform.meta.shared.editor.db;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.ElementVisitor;

@SuppressWarnings("serial")
public class SchemaElement extends AbstractElement {

    private DataSourceElement dataSource;
    private String schemaName;
    private Set<DatabaseTableElement> tables = new HashSet<DatabaseTableElement>();

    public SchemaElement() {
    }

    public SchemaElement(DataSourceElement dataSource) {
        this.dataSource = dataSource;
    }

    public DataSourceElement getDataSource() {
        return dataSource;
    }

    protected void setDataSource(DataSourceElement dataSource) {
        this.dataSource = dataSource;
    }

    public void setDatasource(DataSourceElement dataSource) {
        this.dataSource = dataSource;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public void addTable(DatabaseTableElement table) {
        tables.add(table);
        table.setSchema(this);
    }

    public void removeTable(DatabaseTableElement table) {
        if (!tables.contains(table)) {
            return;
        }
        // удаление прав
        if (getDataSource() != null
            && getDataSource().getApplication() != null) {
            ApplicationElement application = dataSource.getApplication();
            application.removeTableColumnRights(table);
            application.removeTableRights(table);
        }
        tables.remove(table);
        table.setSchema(null);
    }

    public Collection<DatabaseTableElement> getTables() {
        return Collections.unmodifiableSet(tables);
    }

    void setTables(Set<DatabaseTableElement> tables) {
        this.tables = tables;
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

}
