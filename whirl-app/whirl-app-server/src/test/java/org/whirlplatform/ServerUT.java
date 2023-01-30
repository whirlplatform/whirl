package org.whirlplatform;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.config.JndiConfiguration;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.TomcatConnectionProvider;
import org.whirlplatform.server.evolution.EvolutionException;
import org.whirlplatform.server.evolution.EvolutionManager;
import org.whirlplatform.server.evolution.LiquibaseEvolutionManager;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import java.sql.SQLException;

public class ServerUT {

    private static Logger _log = LoggerFactory.getLogger(ServerUT.class);

    private static String DATABASE_VERSION = "13";

    @ClassRule
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        DockerImageName.parse("postgres:" + DATABASE_VERSION))
        .withUsername("postgres")
        //.withNetwork(net)
        .withNetworkAliases("postgresql")
        .withExposedPorts(5432)
        .withFileSystemBind("../../docker/db/postgresql/",
            "/docker-entrypoint-initdb.d/");

    @Before
    public void createDatabase() {

    }

    @Test
    public void migrationTest() throws EvolutionException, ConnectException, SQLException {
        _log.info("Unit test started");

        String alias = "metadata";
        String scriptPath = "org/whirlplatform/sql/changelog.xml";

        postgres.createConnection(alias);



        //postgres.createConnection()
        ConnectionProvider connectionProvider = new TomcatConnectionProvider();
        //connectionProvider.getConnection(alias);
        Configuration configuration = new JndiConfiguration();

        EvolutionManager evolutionManager = new LiquibaseEvolutionManager(connectionProvider, configuration);
        // Whirl apply test
        evolutionManager.applyMetadataEvolution(alias, scriptPath);

        // Applications test
        //evolutionManager.applyApplicationEvolution(alias, scriptPath);
    }

    @Test
    public void rollbackTest() {
        // LiquibaseEvolutionManager var = new LiquibaseEvolutionManager();

        // rollbackApplicationEvolution
        // rollbackMetadataEvolution

    }



}


