package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.DataModifyConfig;
import org.whirlplatform.meta.shared.data.RowModelData;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

public interface DataChanger<T extends AbstractTableElement> {

    RowModelData insert(ClassMetadata metadata, DataModifyConfig config, T table);

    RowModelData update(ClassMetadata metadata, DataModifyConfig config, T table);

    void delete(ClassMetadata metadata, DataModifyConfig config, T table);
}
