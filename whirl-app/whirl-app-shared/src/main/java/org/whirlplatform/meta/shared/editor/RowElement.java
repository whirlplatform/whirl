package org.whirlplatform.meta.shared.editor;

import lombok.Data;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

@SuppressWarnings("serial")
@Data
public class RowElement extends AbstractElement {

    private int row;
    private Double height;

    public RowElement() {
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Override
    public <T extends VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

}
