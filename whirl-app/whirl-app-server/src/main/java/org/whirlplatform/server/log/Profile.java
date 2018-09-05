package org.whirlplatform.server.log;

import java.util.Date;

public interface Profile extends AutoCloseable {

    class MessageWrapper {
		public final Message message;
		public Date startDate;
		public Date endDate;

		public MessageWrapper(Message message) {
			this.message = message;
		}
	}
	
	Date getStartDate();
	
	Date getEndDate();
	
	Message getMessage();
	
	@Override
    void close();
}
