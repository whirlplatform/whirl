package org.whirlplatform.server.metadata;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.empire.db.DBCmpType;
import org.apache.empire.db.DBCommand;
import org.apache.empire.db.DBReader;
import org.whirlplatform.rpc.shared.CustomException;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.driver.db.MetadataDatabase;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetadataProviderImpl implements MetadataProvider {

    private static Logger _log = LoggerFactory.getLogger(MetadataProviderImpl.class);

    private MetadataConfig config;

    private MetadataDatabase database;

    private ConnectionProvider connectionProvider;

    @Inject
    public MetadataProviderImpl(MetadataConfig config, ConnectionProvider connectionProvider) {
        this.config = config;
        this.connectionProvider = connectionProvider;
        database = MetadataDatabase.get();
    }

    protected ConnectionWrapper metadataConnection(ApplicationUser user) {
        try {
            return connectionProvider.getConnection(config.getMetadataAlias(), user);
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
    // String locationBase = "sql/";
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
    // flyway.setDataSource(connectionProvider.getDataSource(config.getMetadataAlias()));
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
        try (ConnectionWrapper connection = metadataConnection()) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new liquibase.Liquibase("sql/changelog.xml", new ClassLoaderResourceAccessor(),
                    database);

            liquibase.update(new Contexts(), new LabelExpression());
        } catch (LiquibaseException | SQLException e) {
            _log.error(e);
            throw new CustomException(e.getMessage());
        }

    }
}
