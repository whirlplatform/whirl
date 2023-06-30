package org.whirlplatform.server.servlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;
import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.config.CoreModule;
import org.whirlplatform.server.config.JndiConfiguration;
import org.whirlplatform.server.config.ServerModule;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.MetadataProvider;
import org.whirlplatform.server.metadata.container.MetadataContainer;
import org.whirlplatform.server.monitor.mbeans.Applications;
import org.whirlplatform.server.monitor.mbeans.Events;
import org.whirlplatform.server.monitor.mbeans.Main;
import org.whirlplatform.server.monitor.mbeans.Users;

public abstract class InjectServletConfig extends GuiceServletContextListener {

    private Configuration configuration = new JndiConfiguration();

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new CoreModule(configuration), new ServerModule());
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        Injector injector = (Injector) servletContextEvent.getServletContext()
            .getAttribute(Injector.class.getName());
        initDatabase(injector);
    }

    private void initDatabase(Injector injector) {
        MetadataProvider mp = injector.getInstance(MetadataProvider.class);
        mp.createDatabaseStructure();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
    }
}
