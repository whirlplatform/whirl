package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.EventResult;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.EventElement;

import java.util.List;

/**
 * Выполняет событие в базе данных
 *
 * @author bedritckiy_mr
 */
public interface EventExecutor {

    /**
     * Выполняет описанную в событии процедуру базы данных
     *
     * @param eventElement - элемент Событие
     * @param params       - входные параметры
     * @return Результат выполнения процедуры
     */
    EventResult executeFunction(EventElement eventElement, List<DataValue> params);
}
