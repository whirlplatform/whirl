package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.EventResult;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.EventElement;

import java.util.List;

/**
 * Выполняет событие в базе данных
 */
public interface EventExecutor {

    /**
     * Выполняет описанную в событии процедуру базы данных.
     *
     * @param eventElement - элемент Событие
     * @param params       - входные параметры
     * @return Результат выполнения процедуры
     */
    EventResult executeFunction(EventElement eventElement, List<DataValue> params);

    /**
     * Выполняет описанный в событии запрос к базе данных.
     *
     * @param eventElement - элемент Событие
     * @param params       - входные параметры
     * @return Результат выполнения запроса
     */
    EventResult executeQuery(EventElement eventElement, List<DataValue> params);
}
