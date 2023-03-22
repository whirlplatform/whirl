package org.whirlplatform.server.driver.multibase.fetch;

import java.util.List;
import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.data.TreeModelData;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

public interface TreeFetcher<T extends AbstractTableElement> {

    List<TreeModelData> getTreeData(ClassMetadata metadata, T table, ClassLoadConfig loadConfig);
}
