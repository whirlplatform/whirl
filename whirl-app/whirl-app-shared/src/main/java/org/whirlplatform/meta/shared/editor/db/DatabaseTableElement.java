package org.whirlplatform.meta.shared.editor.db;


@SuppressWarnings("serial")
public abstract class DatabaseTableElement extends AbstractTableElement {
    protected SchemaElement schema;

    public DatabaseTableElement() {

    }

    public SchemaElement getSchema() {
        return schema;
    }

    protected void setSchema(SchemaElement schema) {
        this.schema = schema;
    }

}
