package org.whirlplatform.integration;

import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class DockerComposeTestRun {

    static final int REDIS_PORT = 6379;
    static final int ELASTICSEARCH_PORT = 9200;

    static final String testComposePath = "src/test/resources/compose-test.yml";
    static final String appComposePath = "src/test/resources/docker-compose.yml";

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File(appComposePath))
//                    .withExposedService("postgresql_1", 5432)
//                    .withExposedService("elasticsearch_1", ELASTICSEARCH_PORT);

                    //.withExposedService("redis_1", REDIS_PORT)
                    //.withExposedService("elasticsearch_1", ELASTICSEARCH_PORT);
                        ;

    @Test
    public void startWhirlWithCompose() throws InterruptedException {

        environment.start();
        
        //String example = environment.getDependencies().toString();
        //System.out.println(example);

        Thread.sleep(100000000);

        assertTrue(true);
    }
}
