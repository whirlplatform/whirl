package org.whirlplatform.server.driver.multibase.fetch;

import java.util.List;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.TreeClassLoadConfig;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

public interface TreeFetcher<T extends AbstractTableElement> {

    List<ListModelData> getTreeData(ClassMetadata metadata, T table, TreeClassLoadConfig loadConfig);
}
