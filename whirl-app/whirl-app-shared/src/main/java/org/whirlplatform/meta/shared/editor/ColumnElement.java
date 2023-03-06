package org.whirlplatform.meta.shared.editor;

import lombok.Data;
import org.whirlplatform.meta.shared.editor.ElementVisitor.VisitContext;

@SuppressWarnings("serial")
@Data
public class ColumnElement extends AbstractElement {

    private int col;
    private Double width;

    public ColumnElement() {
    }

    public int getColumn() {
        return col;
    }

    public void setColumn(int col) {
        this.col = col;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    @Override
    public <T extends VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

}
