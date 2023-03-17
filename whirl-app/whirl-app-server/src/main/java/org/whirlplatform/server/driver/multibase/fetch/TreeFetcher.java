package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.data.TreeModelData;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

import java.util.List;

public interface TreeFetcher<T extends AbstractTableElement> {

    List<TreeModelData> getTreeData(ClassMetadata metadata, T table, ClassLoadConfig loadConfig);
}
