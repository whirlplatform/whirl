package org.whirlplatform.meta.shared.editor.db;

import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.ElementVisitor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class SchemaElement extends AbstractElement {

	private DataSourceElement datasource;
	private String schemaName;
	private Set<DatabaseTableElement> tables = new HashSet<DatabaseTableElement>();

	public SchemaElement() {
	}

	public SchemaElement(DataSourceElement datasource) {
		this.datasource = datasource;
	}

	protected void setDataSource(DataSourceElement database) {
		this.datasource = database;
	}

	public DataSourceElement getDataSource() {
		return datasource;
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
			ApplicationElement application = datasource.getApplication();
			application.removeTableColumnRights(table);
			application.removeTableRights(table);
		}
		tables.remove(table);
		table.setSchema(null);
	}

	public Collection<DatabaseTableElement> getTables() {
        return Collections.unmodifiableSet(tables);
	}

	@Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
		visitor.visit(ctx, this);
	}

}
