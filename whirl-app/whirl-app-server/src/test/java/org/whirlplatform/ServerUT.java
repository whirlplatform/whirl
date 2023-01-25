//package org.whirlplatform;
//
//public class ServerUT {
//
//    @ClassRule
//    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
//        DockerImageName.parse("postgres:" + DATABASE_VERSION))
//        .withUsername("postgres")
//        .withNetwork(net)
//        .withNetworkAliases("postgresql")
//        .withExposedPorts(5432)
//        .withFileSystemBind("../../docker/db/postgresql/",
//            "/docker-entrypoint-initdb.d/");
//
//    @Test
//    public void openSideex() throws IOException, InterruptedException, URISyntaxException, ParseException {
//        LiquibaseEvolutionManager var = new LiquibaseEvolutionManager();
//
//
//    }
//
//
//
//}
