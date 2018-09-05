package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.ClassMetadata;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;

import java.util.Map;

/**
 * Формирует метаданные таблицы базы данных
 *
 * @author bedritckiy_mr
 */
public interface MetadataFetcher<T extends AbstractTableElement> {

    /**
     * Формирует метаданные таблицы базы данных
     *
     * @param table  - табличный элемент
     * @param params - входные параметры
     * @return Метаданные таблицы
     */
    ClassMetadata getClassMetadata(T table, Map<String, DataValue> params);
}
