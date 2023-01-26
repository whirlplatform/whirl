//package org.whirlplatform;
//
//import org.junit.ClassRule;
//import org.junit.Test;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.utility.DockerImageName;
//import org.whirlplatform.server.evolution.LiquibaseEvolutionManager;
//
//public class ServerUT {
//
//    @ClassRule
//    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//        DockerImageName.parse("postgres:13"))
//        .withUsername("postgres")
//        //.withNetwork(net)
//        .withNetworkAliases("postgresql")
//        .withExposedPorts(5432)
//        .withFileSystemBind("../../docker/db/postgresql/",
//            "/docker-entrypoint-initdb.d/");
//
//    @Test
//    public void migrationTest() {
//        LiquibaseEvolutionManager var = new LiquibaseEvolutionManager();

//          folder with migrations
//          asd
//
//
//    }

//    @Test
//    public void rollbackTest() {
//        LiquibaseEvolutionManager var = new LiquibaseEvolutionManager();
//
//
//    }
//
//
//
//}


