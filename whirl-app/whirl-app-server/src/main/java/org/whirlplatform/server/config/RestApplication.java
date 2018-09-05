package org.whirlplatform.server.config;

import com.google.inject.Injector;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.whirlplatform.rpc.server.DataServiceImpl;
import org.whirlplatform.rpc.server.JsonParamConverterProvider;
import org.whirlplatform.rpc.server.RestExceptionMapper;

public class RestApplication extends ResourceConfig {

    public RestApplication() {
        register(new ContainerLifecycleListener() {

            @Override
            public void onStartup(Container container) {
                ServletContainer sContainer = (ServletContainer) container;
                ServiceLocator locator = container.getApplicationHandler().getServiceLocator();
                GuiceBridge.getGuiceBridge().initializeGuiceBridge(locator);
                GuiceIntoHK2Bridge guiceBridge = locator.getService(GuiceIntoHK2Bridge.class);
                Injector injector = (Injector) sContainer.getServletContext().getAttribute(Injector.class.getName());
                guiceBridge.bridgeGuiceInjector(injector);
            }

            @Override
            public void onShutdown(Container container) {
            }

            @Override
            public void onReload(Container container) {
            }
        });
        register(DataServiceImpl.class);
        register(JsonParamConverterProvider.class);
        register(RestExceptionMapper.class);
//		register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME), Level.FINE,
//				Verbosity.PAYLOAD_ANY, 20000));
    }
}
