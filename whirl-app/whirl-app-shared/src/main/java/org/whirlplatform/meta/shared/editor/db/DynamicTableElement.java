package org.whirlplatform.meta.shared.editor.db;

import lombok.Data;
import org.whirlplatform.meta.shared.editor.ElementVisitor;

@SuppressWarnings("serial")
@Data
public class DynamicTableElement extends DatabaseTableElement {

    String metadataFunction;
    String dataFunction;
    String updateFunction;
    String deleteFunction;
    String insertFunction;

    public DynamicTableElement() {

    }

    public DynamicTableElement(SchemaElement schema) {
        setSchema(schema);
    }

    public String getMetadataFunction() {
        return metadataFunction;
    }

    public void setMetadataFunction(String metadataFunction) {
        this.metadataFunction = metadataFunction;
    }

    public String getDataFunction() {
        return dataFunction;
    }

    public void setDataFunction(String dataFunction) {
        this.dataFunction = dataFunction;
    }

    public String getUpdateFunction() {
        return updateFunction;
    }

    public void setUpdateFunction(String updateFunction) {
        this.updateFunction = updateFunction;
    }

    public String getDeleteFunction() {
        return deleteFunction;
    }

    public void setDeleteFunction(String deleteFunction) {
        this.deleteFunction = deleteFunction;
    }

    public String getInsertFunction() {
        return insertFunction;
    }

    public void setInsertFunction(String insertFunction) {
        this.insertFunction = insertFunction;
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

}
