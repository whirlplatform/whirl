package org.whirlplatform.server.driver.multibase.fetch.base;

import org.apache.empire.commons.StringUtils;
import org.apache.empire.db.DBCmpType;
import org.apache.empire.db.DBColumn;
import org.apache.empire.db.DBCommand;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.PlainTableElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.multibase.fetch.AbstractMultiFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;

public abstract class AbstractPlainDataFetcher extends AbstractMultiFetcher {

	public AbstractPlainDataFetcher(ConnectionWrapper connection, DataSourceDriver datasourceDriver) {
		super(connection, datasourceDriver);
	}

	public DataType getIdColumnType(PlainTableElement table) {
		return table.getIdColumn().getType();
	}

    /**
     * Добавляет поля и условия в команды для запроса дерева
     *
     * @param cmd        - внешний запрос
     * @param whereCmd   - команда для добавления ограничений
     * @param loadConfig
     * @param temp
     */
    protected void addTreeCommandPart(DBCommand cmd, DBCommand whereCmd, TreeClassLoadConfig loadConfig,
                                      PlainTableFetcherHelper temp) {
        // Колонка родителя в иерархии

        String parentField = loadConfig.getParentColumn();
        if (!StringUtils.isEmpty(parentField)) {
            RowModelData parent = loadConfig.getParent();
            DBColumn parentColumn = temp.dbTable.getColumn(parentField);
            if (parent != null) {
                whereCmd.where(parentColumn.is(parent.getId()));
            } else {
                whereCmd.where(parentColumn.cmp(DBCmpType.NULL, null));
            }
        }
    }
}
