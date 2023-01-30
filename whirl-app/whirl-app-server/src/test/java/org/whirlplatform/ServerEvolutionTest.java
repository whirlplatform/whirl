package org.whirlplatform;

import org.junit.ClassRule;
import org.junit.Test;
import org.mockito.Mockito;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.db.ConnectException;
import org.whirlplatform.server.db.ConnectionProvider;
import org.whirlplatform.server.driver.multibase.fetch.postgresql.PostgreSQLConnectionWrapper;
import org.whirlplatform.server.evolution.EvolutionException;
import org.whirlplatform.server.evolution.EvolutionManager;
import org.whirlplatform.server.evolution.LiquibaseEvolutionManager;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static org.junit.Assert.assertTrue;

public class ServerEvolutionTest {

    private static Logger _log = LoggerFactory.getLogger(ServerEvolutionTest.class);

    private static String DATABASE_VERSION = "14";

    @ClassRule
    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
        DockerImageName.parse("postgres:" + DATABASE_VERSION))
        .withUsername("postgres")
        .withNetworkAliases("postgresql")
        .withExposedPorts(5432)
            .withCopyToContainer(MountableFile.forHostPath("../../docker/db/postgresql/"),
                    "/docker-entrypoint-initdb.d/")
        ;

    String alias = "metadata";
    String scriptPath = "org/whirlplatform/sql/changelog.xml";
    Properties props;
    Connection connection;
    ConnectionProvider connectionProvider;
    EvolutionManager evolutionManager;
    Configuration configuration;

    @Test
    public void migrationTest()
        throws EvolutionException, ConnectException, SQLException, InterruptedException, IOException {
        _log.info("Migration test started!");

        props = new Properties();
        props.setProperty("user", "whirl");
        props.setProperty("password", "password");

        connection = postgres.getJdbcDriverInstance().connect("jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/whirl", props);
         _log.info(connection.toString());

        connectionProvider = Mockito.mock(ConnectionProvider.class);
        Mockito.when(connectionProvider.getConnection(Mockito.any())).thenReturn(new PostgreSQLConnectionWrapper(alias, connection, null));

        configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.lookup(Mockito.any(String.class))).thenReturn(true);

        evolutionManager = new LiquibaseEvolutionManager(connectionProvider, configuration);
        evolutionManager.applyMetadataEvolution(alias, scriptPath);

        // Check amount of tables
        String str = postgres.execInContainer("psql", "-U", "whirl", "-c", "select count(*) from information_schema.tables where table_schema not in ('information_schema','pg_catalog')").toString();
        String substr = str.substring(str.indexOf("-------") + 7, str.indexOf("(1 row)"));
        String result = substr.replaceAll("\\s", "");
        int amountOfTables = Integer.valueOf(result);
        _log.info("Amount of tables: " + amountOfTables);

        assertTrue("Amount of tables should be greater than 0 !", amountOfTables > 0);

        _log.info("Migration test finished!");
        rollbackTest();
    }

    public void rollbackTest()
        throws InterruptedException, EvolutionException, ConnectException, SQLException, IOException {
        _log.info("Rollback test started!");

        props = new Properties();
        props.setProperty("user", "whirl");
        props.setProperty("password", "password");

        connection = postgres.getJdbcDriverInstance().connect("jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getMappedPort(5432) + "/whirl", props);
        Mockito.when(connectionProvider.getConnection(Mockito.any())).thenReturn(new PostgreSQLConnectionWrapper(alias, connection, null));
        evolutionManager.rollbackMetadataEvolution(alias, scriptPath);

        // Check amount of tables
        String str = postgres.execInContainer("psql", "-U", "whirl", "-c", "select count(*) from information_schema.tables where table_schema not in ('information_schema','pg_catalog')").toString();
        String substr = str.substring(str.indexOf("-------") + 7, str.indexOf("(1 row)"));
        String result = substr.replaceAll("\\s", "");
        int amountOfTables = Integer.valueOf(result);
        _log.info("Amount of tables: " + amountOfTables);
        assertTrue("Amount of tables should be equals to 2 !", amountOfTables == 2);

        _log.info("Rollback test finished!");
    }
}