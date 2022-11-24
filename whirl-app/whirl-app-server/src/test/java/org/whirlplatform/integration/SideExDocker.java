package org.whirlplatform.integration;

import org.junit.ClassRule;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class SideExDocker {

    public static final DockerImageName REDIS_IMAGE = DockerImageName.parse("redis:3.0.2");

    @ClassRule
    public static GenericContainer<?> redis = new GenericContainer<>(REDIS_IMAGE)
            .withExposedPorts(6379);

//    public void openSideex() {
//        try {
//            //Connect to a SideeX WebService server
//            SideeXWebServiceClientAPI wsClient = new SideeXWebServiceClientAPI("http://127.0.0.1:50000", ProtocalType.HTTPS_DISABLE);
//            File file = new File("testcase.zip"); //
//
//            Map<String, File> fileParams = new HashMap<String, File>();
//            fileParams.put(file.getName(), file);
//            System.out.println(wsClient.runTestSuite(fileParams));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
