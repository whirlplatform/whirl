package org.whirlplatform.server.scheduler;

import com.google.inject.Singleton;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import org.quartz.DateBuilder;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.ee.servlet.QuartzInitializerServlet;
import org.quartz.impl.StdSchedulerFactory;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;
import org.whirlplatform.server.session.SessionKillerJob;

@Singleton
public class MainInitializerServlet extends QuartzInitializerServlet {

    private static Logger _log = LoggerFactory.getLogger(MainInitializerServlet.class);

    @Override
    public void init(ServletConfig ctx) throws ServletException {
        super.init(ctx);
        // джоб на убивание устаревших сессий
        JobDetail sessionJob =
            JobBuilder.newJob(SessionKillerJob.class).withIdentity("SessionJob").build();
        Trigger sessionTrigger = TriggerBuilder.newTrigger().forJob(sessionJob).withPriority(5)
            .startAt(DateBuilder.futureDate(31, IntervalUnit.SECOND))
            .withSchedule(SimpleScheduleBuilder.repeatSecondlyForever(30)).build();

        StdSchedulerFactory factory =
            (StdSchedulerFactory) ctx.getServletContext().getAttribute(QUARTZ_FACTORY_KEY);
        try {
            Scheduler scheduler = factory.getScheduler();
            scheduler.scheduleJob(sessionJob, sessionTrigger);
        } catch (SchedulerException e) {
            _log.error(e);
        }
    }
}
