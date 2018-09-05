package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.ClassLoadConfig;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.LoadData;
import org.whirlplatform.meta.shared.data.ListModelData;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

public interface ListFetcher<T extends AbstractTableElement> {

    LoadData<ListModelData> getListData(ClassMetadata metadata, T table, ClassLoadConfig loadConfig);
}
