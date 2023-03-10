package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

import java.util.List;

public interface TreeFetcher<T extends AbstractTableElement> {

    List<ListModelData> getTreeData(ClassMetadata metadata, T table, TreeClassLoadConfig loadConfig);
}
