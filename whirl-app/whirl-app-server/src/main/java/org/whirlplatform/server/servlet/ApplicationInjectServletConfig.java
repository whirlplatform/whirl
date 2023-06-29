package org.whirlplatform.server.servlet;

import com.google.inject.Injector;
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
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.metadata.container.MetadataContainer;
import org.whirlplatform.server.monitor.mbeans.Applications;
import org.whirlplatform.server.monitor.mbeans.Events;
import org.whirlplatform.server.monitor.mbeans.Main;
import org.whirlplatform.server.monitor.mbeans.Users;

public class ApplicationInjectServletConfig extends InjectServletConfig {

    private static Logger _log = LoggerFactory.getLogger(ApplicationInjectServletConfig.class);

    private Set<ObjectName> mBeans = new HashSet<ObjectName>();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        Injector injector = (Injector) servletContextEvent.getServletContext()
            .getAttribute(Injector.class.getName());
        initMBeans(servletContextEvent.getServletContext(), injector);
    }

    // Создание бинов для управления приложением
    protected void initMBeans(ServletContext ctx, Injector injector) {
        try {
            MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();

            String beanPath = ctx.getContextPath();
            if (beanPath == null || beanPath.isEmpty()) {
                beanPath = "ROOT";
            } else {
                beanPath = beanPath.replace("/", "");
            }
            beanPath = "Whirl:type=" + beanPath;

            ObjectName mainName = new ObjectName(beanPath + ",bean=" + Main.OBJECT_NAME);
            mbeanServer.registerMBean(
                new Main(injector.getInstance(MetadataContainer.class)),
                mainName
            );
            mBeans.add(mainName);

            ObjectName appName = new ObjectName(beanPath + ",bean=" + Applications.OBJECT_NAME);
            mbeanServer.registerMBean(new Applications(), appName);
            mBeans.add(appName);

            ObjectName eventsName = new ObjectName(beanPath + ",bean=" + Events.OBJECT_NAME);
            mbeanServer.registerMBean(new Events(), eventsName);
            mBeans.add(eventsName);

            ObjectName usersName = new ObjectName(beanPath + ",bean=" + Users.OBJECT_NAME);
            mbeanServer.registerMBean(new Users(), usersName);
            mBeans.add(usersName);

        } catch (MalformedObjectNameException | InstanceAlreadyExistsException
                 | MBeanRegistrationException | NotCompliantMBeanException e) {
            _log.warn("MBeanServer initialization failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        for (ObjectName bean : mBeans) {
            try {
                mbeanServer.unregisterMBean(bean);
            } catch (MBeanRegistrationException | InstanceNotFoundException e) {
                _log.warn("MBeanServer unregister failed:" + bean.toString(), e);
            }
        }
    }
}
