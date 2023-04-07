package org.whirlplatform.server.driver.multibase.fetch;

import java.io.Closeable;
import java.util.List;
import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Выполняет запрос в базе данных.
 */
public interface QueryExecutor extends Closeable {

    List<DataValue> executeQuery(String sql, List<DataValue> params);

}
