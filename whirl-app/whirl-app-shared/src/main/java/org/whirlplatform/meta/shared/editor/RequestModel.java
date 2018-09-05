package org.whirlplatform.meta.shared.editor;

@SuppressWarnings("serial")
public class RequestModel extends CellGroupModel {

    private String sql;
    private String emptyText;

    public RequestModel() {
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getEmptyText() {
        return emptyText;
    }

    public void setEmptyText(String emptyText) {
        this.emptyText = emptyText;
    }

}
