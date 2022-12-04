package org.whirlplatform.server.log.impl;

import java.util.Date;
import org.whirlplatform.server.log.Message;
import org.whirlplatform.server.log.Profile;
import org.whirlplatform.server.monitor.RunningEvent;
import org.whirlplatform.server.monitor.mbeans.Events;

public class ProfileImpl implements Profile {

    private MessageWrapper message;

    private RunningEvent event;

    /**
     * Конструктор для добавления информации в jmx
     */
    public ProfileImpl(Message message, RunningEvent event) {
        this.event = event;
        if (event != null) {
            Events.addEvent(event);
        }
        this.message = new MessageWrapper(message);
        this.message.startDate = new Date(System.currentTimeMillis());
    }

    public ProfileImpl(Message message) {
        this(message, null);
    }

    @Override
    public Date getStartDate() {
        return message.startDate;
    }

    @Override
    public Date getEndDate() {
        return message.endDate;
    }

    @Override
    public Message getMessage() {
        return message.message;
    }

    @Override
    public void close() {
        message.endDate = new Date(System.currentTimeMillis());
        if (event != null) {
            Events.removeEvent(event);
        }
    }

}
