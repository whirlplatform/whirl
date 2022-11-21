package org.whirlplatform.server.servlet;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.whirlplatform.server.config.Configuration;
import org.whirlplatform.server.config.CoreModule;
import org.whirlplatform.server.config.JndiConfiguration;
import org.whirlplatform.server.config.ServerModule;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.MetadataProvider;
import org.whirlplatform.server.monitor.mbeans.Applications;
import org.whirlplatform.server.monitor.mbeans.Events;
import org.whirlplatform.server.monitor.mbeans.Main;
import org.whirlplatform.server.monitor.mbeans.Users;

import javax.management.*;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.lang.management.ManagementFactory;
import java.util.HashSet;
import java.util.Set;

public class InjectServletConfig extends GuiceServletContextListener {

    private static Logger _log = LoggerFactory.getLogger(InjectServletConfig.class);

    private Configuration configuration = new JndiConfiguration();

    private Set<ObjectName> mBeans = new HashSet<ObjectName>();

    @Override
    protected Injector getInjector() {
        return Guice.createInjector(new CoreModule(configuration), new ServerModule());
    }

    // @Override
    // protected List<? extends Module> getModules(ServletContext arg0) {
    // return Arrays.asList(new CoreModule(configuration), new ServerModule());
    // }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        initMBeans(servletContextEvent.getServletContext());
        initDatabase((Injector) servletContextEvent.getServletContext().getAttribute(Injector.class.getName()));
        _log.info(String.format("Whirl platform context started: %s", servletContextEvent.getServletContext().getServletContextName()));
    }

    // Создание бинов для управления приложением
    private void initMBeans(ServletContext ctx) {
        try {
            MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

            String beanPath = ctx.getContextPath();
            if (beanPath == null || beanPath.isEmpty()) {
                beanPath = "ROOT";
            } else {
                beanPath = beanPath.replace("/", "");
            }
            beanPath = "Whirl:type=" + beanPath;

            ObjectName mainName = new ObjectName(beanPath + ",bean=Main");
            mbeanServer.registerMBean(new Main(), mainName);
            mBeans.add(mainName);

            ObjectName appName = new ObjectName(beanPath + ",bean=Applications");
            mbeanServer.registerMBean(new Applications(), appName);
            mBeans.add(appName);

            ObjectName eventsName = new ObjectName(beanPath + ",bean=Events");
            mbeanServer.registerMBean(new Events(), eventsName);
            mBeans.add(eventsName);

            ObjectName usersName = new ObjectName(beanPath + ",bean=Users");
            mbeanServer.registerMBean(new Users(), usersName);
            mBeans.add(usersName);

        } catch (MalformedObjectNameException | InstanceAlreadyExistsException | MBeanRegistrationException
                | NotCompliantMBeanException e) {
            _log.warn("MBeanServer initialization failed", e);
        }
    }

    private void initDatabase(Injector injector) {
        MetadataProvider mp = injector.getInstance(MetadataProvider.class);
        mp.createDatabaseStructure();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        for (ObjectName bean : mBeans) {
            try {
                mbeanServer.unregisterMBean(bean);
            } catch (MBeanRegistrationException | InstanceNotFoundException e) {
                _log.warn("MBeanServer unregister failed:" + bean.toString(), e);
            }
        }
        super.contextDestroyed(servletContextEvent);
    }
}
