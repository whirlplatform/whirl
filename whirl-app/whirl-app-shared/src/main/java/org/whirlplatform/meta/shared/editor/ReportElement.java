package org.whirlplatform.meta.shared.editor;

import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.component.ComponentType;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ReportElement extends ComponentElement {

	private List<FieldMetadata> fields = new ArrayList<FieldMetadata>();

	public ReportElement() {
		super(ComponentType.ReportType);
	}

	public void setFields(List<FieldMetadata> fields) {
		this.fields = fields;
	}

	public List<FieldMetadata> getFields() {
		return fields;
	}

	public void addField(FieldMetadata field) {
		fields.add(field);
	}

	public void removeField(FieldMetadata field) {
		fields.remove(field);
	}

	@Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
		visitor.visit(ctx, this);
	}
}
