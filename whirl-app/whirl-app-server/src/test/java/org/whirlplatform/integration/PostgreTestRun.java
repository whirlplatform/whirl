package org.whirlplatform.integration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;


public class PostgreTestRun {

    DockerImageName POSTGRES_TEST_IMAGE = DockerImageName.parse("postgres:10");
    @Rule
    public PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(POSTGRES_TEST_IMAGE)
            //.withDatabaseName("whirl")
            //.withUsername("whirl")
            //.withPassword("password")
            //.withNetwork(net)
            .withNetworkAliases("postgres")
            .withExposedPorts(5432);

    @Test
    public void testSimple() throws SQLException, IOException, InterruptedException {
        postgres.start();
        ResultSet resultSet = performQuery(postgres, "SELECT 1");
        int resultSetInt = resultSet.getInt(1);
        assertEquals(resultSetInt, 1);
    }

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
