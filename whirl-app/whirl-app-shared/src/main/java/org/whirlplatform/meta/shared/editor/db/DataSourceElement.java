package org.whirlplatform.meta.shared.editor.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.DatabaseEvolution;
import org.whirlplatform.meta.shared.editor.ElementVisitor;

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

    public ApplicationElement getApplication() {
        return application;
    }

    public void setApplication(ApplicationElement application) {
        this.application = application;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
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

    @JsonProperty
    void setSchemas(Set<SchemaElement> schemas) {
        this.schemas = schemas;
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
