package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.FileValue;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

public interface FileFetcher<T extends AbstractTableElement> {

    FileValue downloadFileFromTable(T table, String column, String rowId);
}
