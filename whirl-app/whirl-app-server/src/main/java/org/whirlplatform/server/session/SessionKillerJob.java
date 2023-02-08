package org.whirlplatform.server.session;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.whirlplatform.server.log.Logger;
import org.whirlplatform.server.log.LoggerFactory;

@DisallowConcurrentExecution
public class SessionKillerJob implements Job {

    private static Logger _log = LoggerFactory
        .getLogger(SessionKillerJob.class);


    @Override
    public void execute(JobExecutionContext ctx) {
        SessionManager.tryKillUnusedSessions();
    }
}
