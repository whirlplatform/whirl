package org.whirlplatform.server.driver.multibase.fetch;

import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.data.DataType;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

public interface DataFetcher<T extends AbstractTableElement> {

    DBReader getTableReader(ClassMetadata metadata, T table, ClassLoadConfig loadConfig);

    DataType getIdColumnType(T table);
}
