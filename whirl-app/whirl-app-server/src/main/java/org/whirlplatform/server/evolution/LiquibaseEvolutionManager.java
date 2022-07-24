package org.whirlplatform.server.evolution;

import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

import javax.inject.Inject;
import java.sql.SQLException;

public class LiquibaseEvolutionManager implements EvolutionManager {

    private static Logger _log = LoggerFactory.getLogger(LiquibaseEvolutionManager.class);

    private ConnectionProvider connectionProvider;
    private Configuration configuration;

    @Inject
    public LiquibaseEvolutionManager(ConnectionProvider connectionProvider, Configuration configuration) {
        this.connectionProvider = connectionProvider;
        this.configuration = configuration;
    }

    @Override
    public void applyApplicationEvolution(String alias, String scriptPath) throws EvolutionException {
        applyEvolution(alias, scriptPath, new FileSystemResourceAccessor());
    }

    @Override
    public void applyMetadataEvolution(String alias, String scriptPath) throws EvolutionException {
        applyEvolution(alias, scriptPath, new ClassLoaderResourceAccessor());
    }

    private void applyEvolution(String alias, String scriptPath, ResourceAccessor resourceAccessor) throws EvolutionException {
        Boolean applyEvolutions = configuration.<Boolean>lookup("Whirl/ds/" + alias
                + "/evolutions/enabled");
        if (applyEvolutions == null || !applyEvolutions) {
            return;
        }

        try (ConnectionWrapper connection = connectionProvider.getConnection(alias)) {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new liquibase.Liquibase(scriptPath, resourceAccessor,
                    database);

            liquibase.update(null, new LabelExpression());
        } catch (LiquibaseException | SQLException | ConnectException e) {
            _log.error(e);
            throw new EvolutionException(e);
        }
    }

}
