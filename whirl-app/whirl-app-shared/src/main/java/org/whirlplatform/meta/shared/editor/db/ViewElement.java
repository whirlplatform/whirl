package org.whirlplatform.meta.shared.editor.db;

import org.whirlplatform.meta.shared.editor.ElementVisitor;

@SuppressWarnings("serial")
public class ViewElement extends SourceElement implements Cloneable {

	private SchemaElement schema;
	private String viewName;

	public ViewElement() {
	}

	public ViewElement(SchemaElement schema) {
		this.schema = schema;
	}

	protected void setSchema(SchemaElement schema) {
		this.schema = schema;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getViewName() {
		return viewName;
	}

	public SchemaElement getSchema() {
		return schema;
	}

	@Deprecated
	public ViewElement clone() {
		ViewElement clone = new ViewElement();
		clone.setId(this.getId());
		clone.setName(this.getName());
		clone.viewName = this.viewName;
		clone.setSource(this.getSource());
		return clone;
	}

	@Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
		visitor.visit(ctx, this);
	}
}
