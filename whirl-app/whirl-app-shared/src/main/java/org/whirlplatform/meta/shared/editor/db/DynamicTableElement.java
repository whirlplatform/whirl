package org.whirlplatform.meta.shared.editor.db;

import org.whirlplatform.meta.shared.editor.ElementVisitor;

@SuppressWarnings("serial")
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
	
	public void setMetadataFunction(String metadataFunction) {
		this.metadataFunction = metadataFunction;
	}
	
	public String getMetadataFunction() {
		return metadataFunction;
	}
	
	public void setDataFunction(String dataFunction) {
		this.dataFunction = dataFunction;
	}
	
	public String getDataFunction() {
		return dataFunction;
	}
	
	public void setUpdateFunction(String updateFunction) {
		this.updateFunction = updateFunction;
	}
	
	public String getUpdateFunction() {
		return updateFunction;
	}
	
	public void setDeleteFunction(String deleteFunction) {
		this.deleteFunction = deleteFunction;
	}
	
	public String getDeleteFunction() {
		return deleteFunction;
	}
	
	public void setInsertFunction(String insertFunction) {
		this.insertFunction = insertFunction;
	}
	
	public String getInsertFunction() {
		return insertFunction;
	}
	
	@Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
		visitor.visit(ctx, this);
	}

}
