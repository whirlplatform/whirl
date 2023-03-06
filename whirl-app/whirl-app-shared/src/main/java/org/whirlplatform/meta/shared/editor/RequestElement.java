package org.whirlplatform.meta.shared.editor;

import lombok.Data;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;

@SuppressWarnings("serial")
@Data
public class RequestElement extends CellRangeElement {

    private DataSourceElement datasource;
    private String sql;
    private PropertyValue emptyText = new PropertyValue(DataType.STRING);

    public RequestElement() {
    }

    public DataSourceElement getDataSource() {
        return datasource;
    }

    public void setDataSource(DataSourceElement datasource) {
        this.datasource = datasource;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public PropertyValue getEmptyText() {
        return emptyText;
    }

    public void setEmptyText(PropertyValue emptyText) {
        this.emptyText = emptyText;
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }
}
