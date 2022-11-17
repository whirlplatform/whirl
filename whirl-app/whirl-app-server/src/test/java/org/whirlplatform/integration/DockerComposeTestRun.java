package org.whirlplatform.integration;

import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class DockerComposeTestRun {

    @ClassRule
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File("../../docker/docker-compose.yml"))
                    .withOptions("--profile image")
                    .withLocalCompose(true)
            ;

    @Test
    public void startWhirlWithCompose() throws InterruptedException {

        //environment.getServiceHost("", );
        //environment.getServicePort("", )

        Thread.sleep(100000000);
        assertTrue(true);
    }
}
