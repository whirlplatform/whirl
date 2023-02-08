package org.whirlplatform.editor.client.dnd;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.Element;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.dnd.core.client.DndDragEnterEvent;
import com.sencha.gxt.dnd.core.client.DndDragLeaveEvent;
import com.sencha.gxt.dnd.core.client.DndDragMoveEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import org.whirlplatform.editor.client.component.surface.Surface;

public class BorderLayoutDropTarget extends DropTarget {

    private BorderLayoutContainer borderLayout;
    private Region region;

    public BorderLayoutDropTarget(BorderLayoutContainer container) {
        super(container);
        this.borderLayout = container;
    }

    @Override
    protected void onDragDrop(DndDropEvent event) {
        // super.onDragDrop(event);
        // Widget widget = (Widget) event.getData();
        // if (borderLayout.getWidgetIndex(widget) > -1) {
        // widget.removeFromParent();
        // }
        // switch (region) {
        // case CENTER:
        // borderLayout.setCenterWidget(widget);
        // break;
        // case NORTH:
        // borderLayout.setNorthWidget(widget);
        // break;
        // case EAST:
        // borderLayout.setEastWidget(widget);
        // break;
        // case SOUTH:
        // borderLayout.setSouthWidget(widget);
        // break;
        // case WEST:
        // borderLayout.setWestWidget(widget);
        // break;
        // }
        // Scheduler.get().scheduleFinally(new ScheduledCommand() {
        // @Override
        // public void execute() {
        // borderLayout.forceLayout();
        // }
        // });
        hideSurface();
        region = null;
    }

    @Override
    protected void onDragLeave(DndDragLeaveEvent event) {
        super.onDragLeave(event);
        hideSurface();
    }

    private void hideSurface() {
        Surface surface = Surface.get();
        surface.setVisible(false);
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
        if (!borderLayout.getElement().isOrHasChild(target)) {
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
        Element el = borderLayout.getElement();
        int height = el.getOffsetHeight();
        int top = el.getAbsoluteTop();
        int y = e.getClientY();

        int width = el.getOffsetWidth();
        int left = el.getAbsoluteLeft();
        int x = e.getClientX();

        int surfaceWidth = 2 * width / 7;
        int surfaceHeight = 2 * height / 7;

        if (y < (top + surfaceHeight)) {
            region = Region.NORTH;
        } else if (y > (top + (height - surfaceHeight))) {
            region = Region.SOUTH;
        } else if (x < (left + surfaceWidth)) {
            region = Region.WEST;
        } else if (x > (left + (width - surfaceWidth))) {
            region = Region.EAST;
        } else {
            region = Region.CENTER;
        }
        showSurface(el);
    }

    protected void showSurface(Element row) {
        Rectangle rect = row.<XElement>cast().getBounds();
        Surface surface = Surface.get();
        surface.getElement().makePositionable(true);
        int surfaceWidth = (2 * rect.getWidth() / 7);
        int surfaceHeight = (2 * rect.getHeight() / 7);
        int x;
        int y;
        int width;
        int height;
        switch (region) {
            case NORTH:
                x = rect.getX();
                y = rect.getY();
                width = rect.getWidth();
                height = surfaceHeight;
                break;
            case SOUTH:
                x = rect.getX();
                y = rect.getY() + rect.getHeight() - surfaceHeight;
                width = rect.getWidth();
                height = surfaceHeight;
                break;
            case WEST:
                x = rect.getX();
                y = rect.getY();
                width = surfaceWidth;
                height = rect.getHeight();
                break;
            case EAST:
                x = rect.getX() + rect.getWidth() - surfaceWidth;
                y = rect.getY();
                width = surfaceWidth;
                height = rect.getHeight();
                break;
            default:
                // CENTER
                x = rect.getX() + surfaceWidth;
                y = rect.getY() + surfaceHeight;
                width = rect.getWidth() - 2 * surfaceWidth;
                height = rect.getHeight() - 2 * surfaceHeight;
                break;
        }
        surface.getElement().setBounds(x, y, width - 1, height - 1);
        surface.show(row);
    }

    public String getLocation() {
        return region.value();
    }

    enum Region {

        CENTER("Center"), NORTH("North"), EAST("East"), SOUTH("South"), WEST(
            "West");

        private final String value;

        Region(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

}
