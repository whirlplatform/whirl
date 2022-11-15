package org.whirlplatform.integration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;


public class PostgreTestRun {

//    @ClassRule
//    public static DockerComposeContainer environment =
//            new DockerComposeContainer(new File("C:/1-must-have/1-workspace/5-job/whirl/docker/docker-compose.yml"));
//                    .withExposedService("redis_1", REDIS_PORT)
//                    .withExposedService("elasticsearch_1", ELASTICSEARCH_PORT);

    //private final String pathNameWar = "src/test/resources/init_postgres.sql";
    private final String pathNameWar = "C:/1-must-have/1-workspace/5-job/whirl/docker/conf/postgresql/context.xml";
    MountableFile warFile = MountableFile.forHostPath(
            Paths.get(pathNameWar), 0777);

    DockerImageName POSTGRES_TEST_IMAGE = DockerImageName.parse("postgres:10");
    @Rule
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_TEST_IMAGE)
            .withDatabaseName("whirl")
            .withUsername("whirl")
            .withPassword("password")
            //.withNetwork(net)
            .withNetworkAliases("postgres")
            .withExposedPorts(5432);

            //
            //.withFileSystemBind("C:/1-must-have/1-workspace/5-job/whirl/docker/conf/postgresql/context.xml", "/usr/local/postgres", BindMode.READ_ONLY)
            //.withDatabaseName("whirl");
//            .withUsername("whirl")
//            .withNetwork(net);

    @Test
    public void testSimple() throws SQLException, IOException, InterruptedException {

//        environment.start();

        postgres.start();
        //postgres.withFileSystemBind("C:/1-must-have/1-workspace/5-job/whirl/docker/conf/postgresql/context.xml", "/usr/local/postgres", BindMode.READ_ONLY);
        postgres.withFileSystemBind("C:/1-must-have/1-workspace/5-job/whirl/docker/docker-compose.yml", "/usr/local/postgres", BindMode.READ_ONLY);
        ResultSet resultSet = performQuery(postgres, "SELECT 1");
        int resultSetInt = resultSet.getInt(1);
        //assertThat(resultSetInt, ).as("A basic SELECT query succeeds").isEqualTo(1);
        assertEquals(resultSetInt, 1);
        //assertHasCorrectExposedAndLivenessCheckPorts(postgres);
    }

//    private void assertHasCorrectExposedAndLivenessCheckPorts(PostgreSQLContainer<?> postgres) {
//        assertThat(postgres.getExposedPorts()).containsExactly(PostgreSQLContainer.POSTGRESQL_PORT);
//        assertThat(postgres.getLivenessCheckPortNumbers())
//                .containsExactly(postgres.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT));
//    }

    protected ResultSet performQuery(JdbcDatabaseContainer<?> container, String sql) throws SQLException {
        DataSource ds = getDataSource(container);
        Statement statement = ds.getConnection().createStatement();
        statement.execute(sql);
        ResultSet resultSet = statement.getResultSet();

        resultSet.next();
        return resultSet;
    }
    protected DataSource getDataSource(JdbcDatabaseContainer<?> container) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(container.getJdbcUrl());
        hikariConfig.setUsername(container.getUsername());
        hikariConfig.setPassword(container.getPassword());
        hikariConfig.setDriverClassName(container.getDriverClassName());
        return new HikariDataSource(hikariConfig);
    }

}
