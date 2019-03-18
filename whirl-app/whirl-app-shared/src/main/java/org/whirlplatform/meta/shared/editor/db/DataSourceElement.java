package org.whirlplatform.meta.shared.editor.db;

import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.DatabaseEvolution;
import org.whirlplatform.meta.shared.editor.ElementVisitor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
public class DataSourceElement extends AbstractElement {

	private ApplicationElement application;

	private String alias;
	private String databaseName;

	private Set<SchemaElement> schemas = new HashSet<SchemaElement>();

	private transient DatabaseEvolution evolution;

	protected DataSourceElement() {
	}

	public DataSourceElement(String alias, String databaseName) {
		this.alias = alias;
		this.databaseName = databaseName;
	}

	public void setApplication(ApplicationElement application) {
		this.application = application;
	}

	public ApplicationElement getApplication() {
		return application;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAlias() {
		return alias;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void addSchema(SchemaElement schema) {
		schema.setDataSource(this);
		schemas.add(schema);
	}

	public void removeSchema(SchemaElement schema) {
		if (!schemas.contains(schema)) {
			return;
		}
		// удаление прав
		if (getApplication() != null) {
			ApplicationElement application = getApplication();
			for (AbstractTableElement t : schema.getTables()) {
				application.removeTableColumnRights(t);
				application.removeTableRights(t);
			}
		}
		schemas.remove(schema);
		schema.setDataSource(null);
	}

	public Collection<SchemaElement> getSchemas() {
		return Collections.unmodifiableSet(schemas);
	}

	public SchemaElement getSchema(String schemaName) {
		if (schemaName == null) {
			throw new IllegalArgumentException("schemaName is null");
		}
		for (SchemaElement s : schemas) {
			if (schemaName.equals(s.getSchemaName())) {
				return s;
			}
		}
		return null;
	}

	public DatabaseEvolution getEvolution() {
		return evolution;
	}

	public void setEvolution(DatabaseEvolution evolution) {
		this.evolution = evolution;
	}

	@Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
		visitor.visit(ctx, this);
	}
	
}
