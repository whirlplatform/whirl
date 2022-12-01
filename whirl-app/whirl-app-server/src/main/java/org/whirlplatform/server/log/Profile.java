package org.whirlplatform.server.log;

import java.util.Date;

public interface Profile extends AutoCloseable {

    Date getStartDate();

    Date getEndDate();

    Message getMessage();

    @Override
    void close();

    class MessageWrapper {
        public final Message message;
        public Date startDate;
        public Date endDate;

        public MessageWrapper(Message message) {
            this.message = message;
        }
    }
}
