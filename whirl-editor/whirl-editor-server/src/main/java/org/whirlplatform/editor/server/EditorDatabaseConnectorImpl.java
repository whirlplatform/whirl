package org.whirlplatform.editor.server;

import java.sql.SQLException;
import javax.inject.Inject;
import org.whirlplatform.editor.shared.RPCException;
import org.whirlplatform.meta.shared.component.RandomUUID;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.db.MetadataDatabase;
import org.whirlplatform.server.global.SrvConstant;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

/**
 * Отвечает за соединение к базе данных, MetadataDatabase
 */
public class EditorDatabaseConnectorImpl implements EditorDatabaseConnector {
    private static Logger log = LoggerFactory.getLogger(EditorDatabaseConnectorImpl.class);
    private ConnectionProvider connectionProvider;
    private MetadataDatabase metadataDatabase;

    @Inject
    public EditorDatabaseConnectorImpl(ConnectionProvider connectionProvider,
                                       MetadataDatabase metadataDatabase) {
        this.connectionProvider = connectionProvider;
        this.metadataDatabase = metadataDatabase;
    }

    @Override
    public ConnectionWrapper metadataConnection(ApplicationUser user) throws RPCException {
        try {
            return connectionProvider.getConnection(SrvConstant.DEFAULT_CONNECTION, user);
        } catch (ConnectException e) {
            log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

    @Override
    public ConnectionWrapper metadataConnection() throws RPCException {
        return metadataConnection(null);
    }

    @Override
    public MetadataDatabase openMetadataDatabase() throws RPCException {
        if (!metadataDatabase.isOpen()) {
            try (ConnectionWrapper connection = metadataConnection()) {
                metadataDatabase.open(connection.getDatabaseDriver(), connection);
            } catch (SQLException | RPCException e) {
                log.error(e);
                throw new RPCException(e.getMessage());
            }
        }
        return metadataDatabase;
    }

    @Override
    public ConnectionWrapper aliasConnection(String alias, ApplicationUser user)
        throws RPCException {
        try {
            return connectionProvider.getConnection(alias, user);
        } catch (ConnectException e) {
            log.error(e);
            throw new RPCException(e.getMessage());
        }
    }

    /**
     * @return запрашивает в базе данных новый уникальный dfobj
     */
    @Override
    public String getNextDfobj(final ApplicationUser user) throws RPCException {
        try (ConnectionWrapper connection = metadataConnection(user)) {
            MetadataDatabase db = openMetadataDatabase();
            Object result = db.getNextSequenceValue("SOBJ", connection);
            return String.valueOf(result);
        } catch (SQLException e) {
            log.error(e);
            throw new RPCException("getNextDfobj error: " + e.getMessage());
        }
    }

    @Override
    public String getNextId() {
        return RandomUUID.uuid();
    }
}
