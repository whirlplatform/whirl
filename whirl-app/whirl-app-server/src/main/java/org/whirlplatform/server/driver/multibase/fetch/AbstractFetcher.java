package org.whirlplatform.server.driver.multibase.fetch;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBDatabase;
import org.apache.empire.db.DBDatabaseDriver;
import org.apache.empire.db.DBReader;
import org.whirlplatform.meta.shared.data.DataValue;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.NamedParamResolver;
import org.whirlplatform.server.login.ApplicationUser;

public class AbstractFetcher {
    private final ConnectionWrapper connection;
    private final DBDatabaseDriver driver;
    private final ApplicationUser user;

    public AbstractFetcher(ConnectionWrapper connection) {
        this.connection = connection;
        this.driver = connection.getDatabaseDriver();
        this.user = connection.getUser();
    }

    public ConnectionWrapper getConnection() {
        return connection;
    }

    public DBDatabaseDriver getDriver() {
        return driver;
    }

    public ApplicationUser getUser() {
        return user;
    }

    public ApplicationElement getApplication() {
        return user.getApplication();
    }

    @SuppressWarnings("serial")
    public DBDatabase createDatabase(final String schemaName) {
        DBDatabase result = new DBDatabase() {
        };
        result.setSchema(schemaName);
        return result;
    }

    public DBDatabase createAndOpenDatabase(final String schemaName) {
        DBDatabase result = createDatabase(schemaName);
        result.open(driver, connection);
        return result;
    }

    public DBReader createAndOpenReader(final DBCommand command) {
        DBReader result = new DBReader();
        result.open(command, connection);
        return result;
    }

    protected String resolveValue(String value, List<DataValue> parameters) {
        if (value == null) {
            return null;
        }
        NamedParamResolver resolver = new NamedParamResolver(getDriver(), value, parameters);
        return resolver.getResultSql();
    }

    public Map<String, String> fromUrlEncoded(final String value) {
        Map<String, String> result = new HashMap<String, String>();
        if (value != null) {
            String[] parameters = value.split("&");
            for (int i = 0; i < parameters.length; i++) {
                if (!parameters[i].isEmpty()) {
                    String[] pair = parameters[i].split("=");
                    if (pair.length != 1) {
                        if (pair[1] == null) {
                            result.put(pair[0], null);
                        } else {
                            try {
                                result.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
                            } catch (UnsupportedEncodingException e) {
                                result.put(pair[0], null);
                            }
                        }
                    }
                }
            }
        }
        return result;
    }
}
