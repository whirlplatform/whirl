package org.whirlplatform.server.driver.multibase.fetch.base;

import org.apache.commons.lang.StringUtils;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.NamedParamResolver;
import org.whirlplatform.server.driver.multibase.fetch.AbstractMultiFetcher;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;

import java.util.Map;

public abstract class AbstractDynamicDataFetcher extends AbstractMultiFetcher {

    public AbstractDynamicDataFetcher(ConnectionWrapper connection, DataSourceDriver factory) {
        super(connection, factory);
    }

    @Override
    protected String resolveValue(String value, Map<String, DataValue> parameters) {
        if (StringUtils.isEmpty(value)) {
            return "";
        }
        NamedParamResolver resolver = new NamedParamResolver(getConnection().getDatabaseDriver(), value, parameters);
        return resolver.getResultSql();
    }
}
