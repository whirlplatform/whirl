package org.whirlplatform.integration;

import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class DockerComposeTestRun {

    static final int REDIS_PORT = 6379;
    static final int ELASTICSEARCH_PORT = 9200;

    static final String testComposePath = "src/test/resources/compose-test.yml";
    static final String appComposePath = "src/test/resources/docker-compose.yml";

    /**/
    private static List<File> files = new ArrayList<>();
    public static List<File> solve() {
        getSubFiles(files, new File("C:\\1-must-have\\1-workspace\\5-job\\whirl\\docker"));
        for (Object file : files.toArray()) {
            System.out.println(((File) file).getAbsolutePath());
        }
        return files;
    }
    private static void getSubFiles(List<File> source, File parent) {
        if (!source.contains(parent)) {
            source.add(parent);
        }
        File[] listFiles = parent.listFiles();
        if(listFiles == null) {
            return;
        }
        for (File file : listFiles) {
            if (file.isDirectory()) {
                getSubFiles(source, file);
            } else {
                if (!source.contains(file)) {
                    source.add(file);
                }
            }
        }
    }
    /**/

    @ClassRule
    public static DockerComposeContainer environment =
//            new DockerComposeContainer(solve())
            new DockerComposeContainer(new File("../../docker/docker-compose.yml"))
                    .withOptions("--profile image")
                    .withLocalCompose(true)
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
