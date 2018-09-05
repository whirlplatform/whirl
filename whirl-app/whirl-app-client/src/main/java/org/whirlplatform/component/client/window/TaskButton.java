package org.whirlplatform.component.client.window;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.ToggleButton;

public class TaskButton extends ToggleButton {

    private Window window;

    public TaskButton(final Window window) {
        super();
        this.window = window;
        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
            @Override
            public void execute() {
                Header header = window.getHeader();
                setText(header.getHTML() == null ? "..." : header.getHTML());
                setIcon(header.getIcon());
            }
        });
    }

    public Window getWindow() {
        return window;
    }

    @Override
    protected void onClick(Event event) {
        super.onClick(event);
        if (!window.isVisible()) {
            window.show();
        } else if (window == WindowManager.get().getActive()) {
            window.minimize();
        } else {
            window.toFront();
        }
    }

}