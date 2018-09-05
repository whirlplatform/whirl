package org.whirlplatform.server.log.impl;

import org.whirlplatform.server.login.ApplicationUser;

public class TableDataMessage extends AbstractMessage {

    private String sql;

    public TableDataMessage(ApplicationUser user, String sql) {
        super(user);
        this.sql = sql;
    }

    @Override
    public String getMessage() {
        StringBuilder builder = new StringBuilder();

        // Определять тип? (table, list, tree)
        builder.append("{\"type\": \"getTableData\", \"sql\": \"").append(sql.replaceAll("\"", "\\\"")).append("\"}");

        return getFullLogMessage(builder.toString());
    }

    public String getSql() {
        return sql;
    }
}
