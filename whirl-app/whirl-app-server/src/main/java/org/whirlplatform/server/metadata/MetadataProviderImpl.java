package org.whirlplatform.server.metadata;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.empire.db.DBCmpType;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.db.MetadataDatabase;
import org.whirlplatform.server.evolution.EvolutionException;
import org.whirlplatform.server.evolution.EvolutionManager;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

public class MetadataProviderImpl implements MetadataProvider {

    private static Logger _log = LoggerFactory.getLogger(MetadataProviderImpl.class);

    private MetadataConfig metadataConfig;

    private MetadataDatabase database;

    private ConnectionProvider connectionProvider;

    private Configuration configuration;

    private EvolutionManager evolutionManager;

    @Inject
    public MetadataProviderImpl(MetadataConfig metadataConfig,
                                ConnectionProvider connectionProvider,
                                Configuration configuration, EvolutionManager evolutionManager) {
        this.metadataConfig = metadataConfig;
        this.connectionProvider = connectionProvider;
        this.configuration = configuration;
        this.evolutionManager = evolutionManager;
        database = MetadataDatabase.get();
    }

    protected ConnectionWrapper metadataConnection(ApplicationUser user) {
        try {
            return connectionProvider.getConnection(metadataConfig.getMetadataAlias(), user);
        } catch (ConnectException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }

    protected ConnectionWrapper metadataConnection() {
        return metadataConnection(null);
    }

    protected MetadataDatabase openMetadataDatabase(ConnectionWrapper connection) {
        if (!database.isOpen()) {
            synchronized (database) {
                database.open(connection.getDatabaseDriver(), connection);
            }
        }
        return database;
    }

    @Override
    public List<String> getUserApplications(ApplicationUser user) {
        List<String> result = new ArrayList<>();
        try (ConnectionWrapper conn = metadataConnection(user)) {
            MetadataDatabase db = openMetadataDatabase(conn);
            DBCommand select = db.createCommand();
            select.select(db.WHIRL_USER_APPLICATIONS.APPLICATION_CODE);
            select.where(db.WHIRL_USER_APPLICATIONS.R_WHIRL_USERS.is(user.getId()));
            select.where(db.WHIRL_USER_APPLICATIONS.DELETED.cmp(DBCmpType.NULL, null));
            DBReader reader = new DBReader();
            try {
                reader.open(select, conn);
                while (reader.moveNext()) {
                    result.add(reader.getString(db.WHIRL_USER_APPLICATIONS.APPLICATION_CODE));
                }
            } finally {
                reader.close();
            }
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
        return result;
    }

    @Override
    public List<String> getUserGroups(ApplicationUser user) {
        List<String> result = new ArrayList<>();
        try (ConnectionWrapper conn = metadataConnection(user)) {
            MetadataDatabase db = openMetadataDatabase(conn);
            DBCommand select = db.createCommand();
            select.select(db.WHIRL_USER_GROUPS.GROUP_CODE);
            select.where(db.WHIRL_USER_GROUPS.R_WHIRL_USERS.is(user.getId()));
            select.where(db.WHIRL_USER_GROUPS.DELETED.cmp(DBCmpType.NULL, null));
            DBReader reader = new DBReader();
            try {
                reader.open(select, conn);
                while (reader.moveNext()) {
                    result.add(reader.getString(db.WHIRL_USER_GROUPS.GROUP_CODE));
                }
            } finally {
                reader.close();
            }
        } catch (SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
        return result;
    }

    // @Override
    // public void createDatabaseStructure() {
    // String locationBase = "org.whirlplatform.sql/";
    // List<String> locations = new ArrayList<>();
    // locations.add(locationBase + "common");
    // try(ConnectionWrapper conn = metadataConnection()) {
    // if (conn instanceof OracleConnectionWrapper) {
    // locations.add(locationBase + "oracle");
    // } else if (conn instanceof PostgreSQLConnectionWrapper) {
    // locations.add(locationBase + "postgresql");
    // }
    // } catch (SQLException e) {
    // _log.error(e);
    // throw new CustomException(e.getMessage());
    // }
    //
    // try {
    // Flyway flyway = new Flyway();
    // flyway.setDataSource(connectionProvider.getDataSource(metadataConfig.getMetadataAlias()));
    // flyway.setLocations(locations.toArray(new String[0]));
    // flyway.migrate();
    // } catch (ConnectException e) {
    // _log.error(e);
    // throw new CustomException(e.getMessage());
    // }
    // }

    // @Override
    // public void createDatabaseStructure() {
    // try (ConnectionWrapper connection = metadataConnection()) {
    // MetadataDatabase db = openMetadataDatabase(connection);
    // DBSQLScript script = new DBSQLScript();
    // db.getCreateDDLScript(connection.getDatabaseDriver(), script);
    // _log.debug(script.toString());
    // script.executeAll(connection.getDatabaseDriver(), connection);
    // } catch (SQLException | EmpireException e) {
    // _log.error(e);
    // throw new CustomException(e.getMessage());
    // }
    // }

    @Override
    public void createDatabaseStructure() {
        try {
            evolutionManager.applyMetadataEvolution(metadataConfig.getMetadataAlias(),
                "org/whirlplatform/sql/changelog.xml");
        } catch (EvolutionException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }
    }


}
