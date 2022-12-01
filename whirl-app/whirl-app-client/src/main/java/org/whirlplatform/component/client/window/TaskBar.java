package org.whirlplatform.component.client.window;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.AttachEvent.Handler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import java.util.Iterator;
import java.util.Set;

public class TaskBar extends ToolBar {

    public TaskBar() {
        super();
        addAttachHandler(new Handler() {

            @Override
            public void onAttachOrDetach(AttachEvent event) {
                if (event.isAttached()) {
                    WindowManager.get().registerTaskBar(TaskBar.this);
                } else {
                    WindowManager.get().unregisterTaskBar(TaskBar.this);
                }
            }

        });
        setMinButtonWidth(30);
    }

//    public void syncWindows(Iterator<Window> windows) {
//        int i = 0;
//        while (windows.hasNext()) {
//            Window w = windows.next();
//            boolean equal = false;
//            if (getWidgetCount() < i + 1
//                    || (equal = ((TaskButton) getWidget(i)).getWindow() == w)) {
//                insert(new TaskButton(w), i);
//
//            }
//            if (equal) {
//                remove(i + 1);
//            }
//        }
//        while (i < getWidgetCount()) {
//            remove(i);
//            i++;
//        }
//    }

    public void syncWindows(Set<Window> windows) {
        Iterator<Widget> iterRemove = iterator();
        while (iterRemove.hasNext()) {
            TaskButton b = (TaskButton) iterRemove.next();
            if (!windows.contains(b.getWindow())) {
                iterRemove.remove();
            }
        }

        for (Window w : windows) {
            boolean exists = false;
            Iterator<Widget> iterInsert = iterator();
            while (iterInsert.hasNext()) {
                if (((TaskButton) iterInsert.next()).getWindow() == w) {
                    exists = true;
                }
            }
            if (!exists) {
                insert(new TaskButton(w), getWidgetCount());
            }
        }

        // Просто forceLayout не всегда работает правильно(если открыть окно, которое уже открыто)
        Scheduler.get().scheduleFinally(forceLayoutCommand);
    }

    public void setActive(Window window) {
        Iterator<Widget> iter = iterator();
        while (iter.hasNext()) {
            TaskButton btn = (TaskButton) iter.next();
            if (btn.getWindow() == window) {
                btn.setValue(true);
            } else {
                btn.setValue(false);
            }
        }
    }

}
