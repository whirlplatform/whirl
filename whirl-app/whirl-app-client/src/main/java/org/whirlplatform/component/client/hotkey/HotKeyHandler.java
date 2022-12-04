package org.whirlplatform.component.client.hotkey;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ModalPanel;
import java.util.Iterator;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.KeyPressEvent;

public class HotKeyHandler implements NativePreviewHandler {

    private Component component;

    private ComponentBuilder builder;

    private int keyCode;

    private boolean isAlt = false;

    private boolean isShift = false;

    private boolean isCtrl = false;

    public HotKeyHandler(int keyCode, ComponentBuilder builder) {
        this.keyCode = keyCode;
        this.builder = builder;
        this.component = builder.getComponent();
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public boolean isAlt() {
        return isAlt;
    }

    public void setAlt(boolean isAlt) {
        this.isAlt = isAlt;
    }

    public boolean isShift() {
        return isShift;
    }

    public void setShift(boolean isShift) {
        this.isShift = isShift;
    }

    public boolean isCtrl() {
        return isCtrl;
    }

    public void setCtrl(boolean isCtrl) {
        this.isCtrl = isCtrl;
    }

    @Override
    public void onPreviewNativeEvent(NativePreviewEvent event) {
        if (event.getTypeInt() == Event.getTypeInt(KeyDownEvent.getType()
                .getName())) {
            NativeEvent nativeEvent = event.getNativeEvent();
            boolean alt = nativeEvent.getAltKey();
            boolean ctrl = nativeEvent.getCtrlKey();
            boolean shift = nativeEvent.getShiftKey();

            if (nativeEvent.getKeyCode() == keyCode && alt == isAlt
                    && ctrl == isCtrl && shift == isShift && isAllowed()) {
                builder.fireEvent(new KeyPressEvent());
            }
        }
    }

    private boolean isAllowed() {
        return (component == null || component.getElement().isVisible(true))
                && !hasModality();
    }

    private boolean hasModality() {
        Iterator<Widget> iter = RootPanel.get().iterator();
        while (iter.hasNext()) {
            Widget w = iter.next();
            if (w instanceof ModalPanel) {
                int zModal = ((ModalPanel) w).getElement().getZIndex();
                String zComp = component.getElement().getStyle().getZIndex();
                zComp = zComp == null || zComp.isEmpty() ? "0" : component
                        .getElement().getStyle().getZIndex();
                int zIndex = Integer.valueOf(zComp);
                if (zIndex > zModal) {
                    return false;
                }

                Widget parent = component.getParent();
                while (parent != null && parent != RootPanel.get()) {
                    zComp = parent.getElement().getStyle() != null ? parent
                            .getElement().getStyle().getZIndex() : null;
                    zComp = zComp == null || zComp.isEmpty() ? "0" : parent
                            .getElement().getStyle().getZIndex();
                    zIndex = Integer.valueOf(zComp);
                    if (zIndex > zModal) {
                        return false;
                    }
                    parent = parent.getParent();
                }
                return true;
            }
        }
        return false;
    }

}
