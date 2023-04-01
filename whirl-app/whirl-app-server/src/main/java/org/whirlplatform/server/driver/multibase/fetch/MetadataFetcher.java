package org.whirlplatform.server.driver.multibase.fetch;

import java.util.List;
import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

/**
 * Формирует метаданные таблицы базы данных
 */
public interface MetadataFetcher<T extends AbstractTableElement> {

    /**
     * Формирует метаданные таблицы базы данных
     *
     * @param table  - табличный элемент
     * @param params - входные параметры
     * @return Метаданные таблицы
     */
    ClassMetadata getClassMetadata(T table, List<DataValue> params);
}
