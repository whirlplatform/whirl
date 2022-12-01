package org.whirlplatform.editor.client.dnd;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Widget;
import org.whirlplatform.editor.client.component.surface.DefaultSurfaceAppearance;
import org.whirlplatform.editor.client.component.surface.Surface;
import org.whirlplatform.editor.client.dnd.MouseSelectionEvent.HasMouseSelectionHandlers;
import org.whirlplatform.editor.client.dnd.MouseSelectionEvent.MouseSelectionHandler;

public class MouseSelectionWrapper implements HasMouseSelectionHandlers {

    private static final int DISTANCE = 5;

    private boolean enabled;
    private boolean selection = false;
    private int startX;
    private int startY;

    private Widget widget;
    private Surface surface;
    private HandlerRegistration mouseUpRegistration;
    private HandlerRegistration mouseMoveRegistration;
    private NativePreviewHandler mouseMovePreview = new NativePreviewHandler() {

        @Override
        public void onPreviewNativeEvent(NativePreviewEvent event) {
            if (Event.ONMOUSEMOVE == event.getTypeInt()) {
                onMouseMove(event.getNativeEvent());
            }
        }

    };
    private HandlerManager handlerManager;
    private HandlerRegistration keyDownRegistration;
    private NativePreviewHandler mouseUpPreview = new NativePreviewHandler() {

        @Override
        public void onPreviewNativeEvent(NativePreviewEvent event) {
            if (Event.ONMOUSEUP == event.getTypeInt()) {
                onMouseUp(event.getNativeEvent());
            }
        }

    };
    private NativePreviewHandler keyPreview = new NativePreviewHandler() {

        @Override
        public void onPreviewNativeEvent(NativePreviewEvent event) {
            int key = event.getNativeEvent().getKeyCode();
            if (key == KeyCodes.KEY_ESCAPE) {
                MouseSelectionWrapper.this.onKeyEscape(event);
            }
        }

    };

    public MouseSelectionWrapper(Widget widget) {
        this.widget = widget;
        this.widget.addDomHandler(new MouseDownHandler() {
            @Override
            public void onMouseDown(MouseDownEvent event) {
                MouseSelectionWrapper.this.onMouseDown(event);
            }
        }, MouseDownEvent.getType());
        surface = new Surface(new DefaultSurfaceAppearance());
        surface.getElement().getStyle().setZIndex(9999);
    }

    private void addMouseUpHandler() {
        mouseUpRegistration = Event.addNativePreviewHandler(mouseUpPreview);
    }

    private void removeMouseUpHandler() {
        if (mouseUpRegistration != null) {
            mouseUpRegistration.removeHandler();
        }
    }

    private void addMouseMoveHandler() {
        mouseMoveRegistration = Event.addNativePreviewHandler(mouseMovePreview);
    }

    private void removeMouseMoveHandler() {
        if (mouseMoveRegistration != null) {
            mouseMoveRegistration.removeHandler();
        }
    }

    private void addKeyDownHandler() {
        keyDownRegistration = Event.addNativePreviewHandler(keyPreview);
    }

    private void removeKeyDownHandler() {
        if (keyDownRegistration != null) {
            keyDownRegistration.removeHandler();
        }
    }

    protected void onMouseDown(MouseDownEvent event) {
        if (!enabled) {
            return;
        }
        // deselect
        ensureHandlers().fireEvent(
                new MouseSelectionEvent(false, event.getX(), event.getY(), -1,
                        -1));
        hideSurface();
        if (!selection) {
            startX = event.getClientX();
            startY = event.getClientY();
            addMouseMoveHandler();
            addMouseUpHandler();
            addKeyDownHandler();
        }
    }

    protected void onMouseUp(NativeEvent event) {
        if (selection) {
            int endX = event.getClientX();
            int endY = event.getClientY();
            int x = Math.min(startX, endX);
            int y = Math.min(startY, endY);
            int width = Math.abs(startX - endX);
            int height = Math.abs(startY - endY);
            selection = false;
            hideSurface();
            ensureHandlers().fireEvent(
                    new MouseSelectionEvent(true, x, y, width, height));
        }
        removeMouseMoveHandler();
        removeMouseUpHandler();
        removeKeyDownHandler();
    }

    protected void onMouseMove(NativeEvent event) {
        if (!enabled) {
            return;
        }
        int endX = event.getClientX();
        int endY = event.getClientY();
        if (endX < widget.getAbsoluteLeft() ||
                endX > widget.getAbsoluteLeft() + widget.getOffsetWidth()
                || endY < widget.getAbsoluteTop() ||
                endY > widget.getAbsoluteTop() + widget.getOffsetHeight()) {
            return;
        }
        if (!selection
                && (Math.abs(startX - endX) > DISTANCE || Math.abs(startY
                - endY) > DISTANCE)) {
            event.preventDefault();
            selection = true;
            surface.show();
        }
        if (selection) {
            placeSurface(startX, startY, endX, endY);
        }
    }

    protected void onKeyEscape(NativePreviewEvent event) {
        selection = false;
        hideSurface();
        removeMouseMoveHandler();
        removeMouseUpHandler();
        removeKeyDownHandler();
    }

    private HandlerManager ensureHandlers() {
        if (handlerManager == null) {
            handlerManager = new HandlerManager(null);
        }
        return handlerManager;
    }

    @Override
    public HandlerRegistration addMouseSelectionHandler(
            MouseSelectionHandler handler) {
        return ensureHandlers().addHandler(MouseSelectionEvent.getType(),
                handler);
    }

    private void hideSurface() {
        surface.hide();
    }

    private void placeSurface(int startX, int startY, int endX, int endY) {
        int x = Math.min(startX, endX);
        int y = Math.min(startY, endY);
        int width = Math.abs(startX - endX);
        int height = Math.abs(startY - endY);
        surface.getElement().setBounds(x, y, width, height);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (!enabled) {
            selection = false;
            hideSurface();
        }
    }

}
