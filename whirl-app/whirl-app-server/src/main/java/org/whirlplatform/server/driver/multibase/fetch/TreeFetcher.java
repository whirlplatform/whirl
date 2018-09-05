package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

import java.util.List;

public interface TreeFetcher<T extends AbstractTableElement> {

    List<RowModelData> getTreeData(ClassMetadata metadata, T table, TreeClassLoadConfig loadConfig);
}
