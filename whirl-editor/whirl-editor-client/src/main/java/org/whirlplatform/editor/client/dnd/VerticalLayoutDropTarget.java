package org.whirlplatform.editor.client.dnd;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.dnd.core.client.DND.Feedback;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragLeaveEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.dnd.core.client.Insert;
import com.sencha.gxt.widget.core.client.container.InsertResizeContainer;

public class VerticalLayoutDropTarget extends DropTarget {

    private InsertResizeContainer verticalLayout;

    private int insertIndex;

    private boolean before;

    public VerticalLayoutDropTarget(InsertResizeContainer container) {
        super(container);
        this.verticalLayout = container;
    }

    @Override
    protected void onDragDrop(DndDropEvent event) {
        // super.onDragDrop(event);
        // Widget widget = (Widget) event.getData();
        //
        // if (feedback == Feedback.APPEND) {
        // verticalLayout.add(widget);
        // } else {
        // if (insertIndex == -1) {
        // insertIndex = 0;
        // }
        // verticalLayout.insert(widget, insertIndex);
        // }
        //
        // Scheduler.get().scheduleFinally(new ScheduledCommand() {
        // @Override
        // public void execute() {
        // verticalLayout.forceLayout();
        // }
        // });

        insertIndex = -1;
    }

    @Override
    protected void onDragLeave(DndDragLeaveEvent event) {
        super.onDragLeave(event);
        Insert insert = Insert.get();
        insert.setVisible(false);
    }

    @Override
    protected void onDragEnter(DndDragEnterEvent event) {
        super.onDragEnter(event);
        event.setCancelled(true);
        event.getStatusProxy().setStatus(true);
    }

    @Override
    protected void onDragMove(DndDragMoveEvent event) {
        XElement target = event.getDragMoveEvent().getNativeEvent()
            .getEventTarget().cast();
        if (!verticalLayout.getElement().isOrHasChild(target)) {
            event.setCancelled(true);
            event.getStatusProxy().setStatus(false);
        } else {
            event.setCancelled(false);
            event.getStatusProxy().setStatus(true);
        }
    }

    @Override
    protected void showFeedback(DndDragMoveEvent event) {
        event.getStatusProxy().setStatus(true);
        NativeEvent e = event.getDragMoveEvent().getNativeEvent().cast();
        Element el = event.getDragMoveEvent().getNativeEvent()
            .getEventTarget().cast();
        Widget w = verticalLayout.findWidget(el);
        if (w != null
            && (feedback == Feedback.INSERT || feedback == Feedback.BOTH)) {
            int height = el.getOffsetHeight();
            int mid = height / 2;
            mid += el.getAbsoluteTop();
            int y = e.getClientY();
            before = y < mid;
            insertIndex = verticalLayout.getWidgetIndex(w) + 1;
            if (before && insertIndex > 0) {
                insertIndex = insertIndex - 1;
            }
        } else {
            before = false;
            insertIndex = verticalLayout.getWidgetCount();
            if (insertIndex > 0) {
                w = verticalLayout.getWidget(insertIndex - 1);
            }
        }
        if (w != null) {
            showInsert(w.getElement());
        }
    }

    private void showInsert(Element row) {
        Insert insert = Insert.get();
        insert.show(row);
        Rectangle rect = row.<XElement>cast().getBounds();
        int y;
        if (before) {
            y = rect.getY() - 4;
        } else {
            y = rect.getY() + rect.getHeight();
        }
        insert.getElement().makePositionable(true);
        insert.getElement().setBounds(rect.getX(), y, rect.getWidth(), 6);
    }

    public int getIndex() {
        return insertIndex;
    }

}
