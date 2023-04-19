package org.whirlplatform.server.driver.multibase.fetch.oracle;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.empire.db.DBDatabase;
import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.meta.shared.editor.db.TableColumnElement;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.AbstractFetcher;
import org.whirlplatform.server.driver.multibase.fetch.FileFetcher;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

public class BasePlainFileFetcher extends AbstractFetcher
    implements FileFetcher<PlainTableElement> {
    private static Logger _log = LoggerFactory.getLogger(BasePlainFileFetcher.class);

    public BasePlainFileFetcher(final ConnectionWrapper connection) {
        super(connection);
    }

    @Override
    public FileValue downloadFileFromTable(PlainTableElement table, String column, String rowId) {
        TableColumnElement col = table.getColumn(column);
        if (col == null) {
            throw new CustomException("Column not exists");
        }
        FileValue result = null;
        DBDatabase dbDatabase = createAndOpenDatabase(table.getSchema().getSchemaName());
        try {
            final String query = createSelectQuery(table, column, rowId);
            ResultSet rs = dbDatabase.executeQuery(query, null, false, getConnection());
            if (rs.next()) {
                InputStream inputStream = rs.getBinaryStream(1);

                String fname = rs.getString(2);
                result = new FileValue();
                result.setInputStream(inputStream);
                result.setName(fname);
            }
            return result;
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    private String createTableFullName(PlainTableElement table) {
        return String.format("%s.%s", table.getSchema().getSchemaName(), table.getTableName());
    }

    private String createSelectQuery(PlainTableElement table, String column, String rowId) {
        final String SELECT = "select %s, %s from %s where %s=%s";
        final String columnName = table.getColumn(column).getColumnName();
        final String labelName = table.getColumn(column).getLabelExpression();
        final String fullName = createTableFullName(table);
        final String idColumnName = table.getIdColumn().getColumnName();
        String result =
            String.format(SELECT, columnName, labelName, fullName, idColumnName, rowId);
        return result;
    }
}
