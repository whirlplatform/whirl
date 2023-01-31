package org.whirlplatform;

import liquibase.pro.packaged.C;
import org.apache.empire.db.DBDatabaseDriver;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.config.JndiConfiguration;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.db.ConnectionWrapper;
import org.whirlplatform.server.db.TomcatConnectionProvider;
import org.whirlplatform.server.driver.multibase.fetch.DataSourceDriver;
import org.whirlplatform.server.driver.multibase.fetch.postgresql.PostgreSQLConnectionWrapper;
import org.whirlplatform.server.evolution.EvolutionException;
import org.whirlplatform.server.evolution.EvolutionManager;
import org.whirlplatform.server.evolution.LiquibaseEvolutionManager;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.login.ApplicationUser;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ServerUT {

    private static Logger _log = LoggerFactory.getLogger(ServerUT.class);

    private static String DATABASE_VERSION = "14";

    @ClassRule
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        DockerImageName.parse("postgres:" + DATABASE_VERSION))
        .withUsername("postgres")
        .withNetworkAliases("postgresql")
        .withExposedPorts(5432)
        .withFileSystemBind("../../docker/db/postgresql/",
            "/docker-entrypoint-initdb.d/");

    @Before
    public void createDatabase() {

    }

    @Test
    public void migrationTest()
        throws EvolutionException, ConnectException, SQLException, InterruptedException, IOException {
        _log.info("Unit test started");
        String alias = "metadata";
        String scriptPath = "org/whirlplatform/sql/changelog.xml";
        _log.info(postgres.getJdbcUrl());
        Properties props = new Properties();
        props.setProperty("user", "whirl");
        props.setProperty("password", "password");
        Connection connection = postgres.getJdbcDriverInstance().connect("jdbc:postgresql://localhost:" + postgres.getMappedPort(5432) + "/whirl", props);
        _log.info(connection.toString());

        //postgres.execInContainer("psql", "-U", "whirl", "-c", "CREATE SCHEMA whirl AUTHORIZATION whirl;");
        //postgres.execInContainer("psql", "-U", "whirl", "-c", "SET search_path TO whirl;");

        //Thread.sleep(100000000l);

        //TestConnectionWrapper tc = new TestConnectionWrapper(alias, connection, null);
        ConnectionProvider connectionProvider = Mockito.mock(ConnectionProvider.class);
        Mockito.when(connectionProvider.getConnection(Mockito.any())).thenReturn(new PostgreSQLConnectionWrapper(alias, connection, null));

        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.lookup(Mockito.any(String.class))).thenReturn(true);

        EvolutionManager evolutionManager = new LiquibaseEvolutionManager(connectionProvider, configuration);
        evolutionManager.applyMetadataEvolution(alias, scriptPath);

        connection = postgres.getJdbcDriverInstance().connect("jdbc:postgresql://localhost:" + postgres.getMappedPort(5432) + "/whirl", props);
        Mockito.when(connectionProvider.getConnection(Mockito.any())).thenReturn(new PostgreSQLConnectionWrapper(alias, connection, null));
        evolutionManager.rollbackMetadataEvolution(alias, scriptPath);

        Thread.sleep(100000000l);

        _log.info("Unit test finished");
    }

    @Test
    public void rollbackTest() {
        // LiquibaseEvolutionManager var = new LiquibaseEvolutionManager();
        // rollbackApplicationEvolution
        // rollbackMetadataEvolution
    }
}


