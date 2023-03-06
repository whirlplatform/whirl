package org.whirlplatform.meta.shared.editor;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.whirlplatform.meta.shared.FieldMetadata;
import org.whirlplatform.meta.shared.component.ComponentType;

@SuppressWarnings("serial")
@Data
public class ReportElement extends ComponentElement {

    private List<FieldMetadata> fields = new ArrayList<FieldMetadata>();

    public ReportElement() {
        super(ComponentType.ReportType);
    }

    public List<FieldMetadata> getFields() {
        return fields;
    }

    public void setFields(List<FieldMetadata> fields) {
        this.fields = fields;
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
