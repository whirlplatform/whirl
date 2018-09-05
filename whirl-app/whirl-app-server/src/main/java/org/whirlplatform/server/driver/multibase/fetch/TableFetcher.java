package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

public interface TableFetcher<T extends AbstractTableElement> {

    LoadData<RowModelData> getTableData(ClassMetadata metadata, T table, ClassLoadConfig loadConfig);
}
