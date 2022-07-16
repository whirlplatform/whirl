package org.whirlplatform.server.driver.multibase.fetch;

import org.whirlplatform.meta.shared.data.DataValue;

import java.io.Closeable;
import java.util.Map;

/**
 * Выполняет запрос в базе данных.
 */
public interface QueryExecutor extends Closeable {

    Map<String, DataValue> executeQuery(String sql, Map<String, DataValue> params);

}
