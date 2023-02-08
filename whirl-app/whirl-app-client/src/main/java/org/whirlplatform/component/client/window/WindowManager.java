package org.whirlplatform.component.client.window;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.ActivateEvent;
import com.sencha.gxt.widget.core.client.event.ActivateEvent.ActivateHandler;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent;
import com.sencha.gxt.widget.core.client.event.DeactivateEvent.DeactivateHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent.MinimizeHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WindowManager {

    private static WindowManager _instance;
    private final DesktopLayout layout = new CascadeDesktopLayout();
    private final Map<Window, WindowBuilder> builders = new HashMap<Window, WindowBuilder>();
    private final Map<Window, WindowDescription> windows = new LinkedHashMap<Window, WindowDescription>();
    private final Set<TaskBar> taskBars = new HashSet<TaskBar>();
    private Window activeWindow;
    private WindowHandler handler;

    public static WindowManager get() {
        if (_instance == null) {
            _instance = new WindowManager();
        }
        return _instance;
    }

    private WindowHandler ensureHandler() {
        if (handler == null) {
            handler = new WindowHandler();
        }
        return handler;
    }

    public void add(WindowBuilder builder) {
        Window w = builder.getRealComponent();
        builders.put(w, builder);
        add(w);
    }

    public void add(Window window) {
        if (contains(window)) {
            return;
        }
        windows.put(window, new WindowDescription());
        getWindowDescription(window).addHandlerRegistration(
            window.addActivateHandler(ensureHandler()));
        getWindowDescription(window).addHandlerRegistration(
            window.addDeactivateHandler(ensureHandler()));
        getWindowDescription(window).addHandlerRegistration(
            window.addMinimizeHandler(ensureHandler()));
        getWindowDescription(window).addHandlerRegistration(
            window.addHideHandler(ensureHandler()));
        getWindowDescription(window).addHandlerRegistration(
            window.addShowHandler(ensureHandler()));
        syncTaskBars();
    }

    private void remove(Window window) {
        windows.remove(window);
        builders.remove(window);
    }

    private boolean contains(Window window) {
        return windows.containsKey(window);
    }

    public boolean isMinimized(Window window) {
        return getWindowDescription(window).isMinimized();
    }

    private void setMinimized(Window window, boolean minimized) {
        getWindowDescription(window).setMinimized(minimized);
    }

    private WindowDescription getWindowDescription(Window window) {
        if (!windows.containsKey(window)) {
            throw new IllegalArgumentException("No such window registered");
        }
        return windows.get(window);
    }

    public void removeWindow(Window window) {
        if (contains(window)) {
            getWindowDescription(window).removeHandlerRegistrations();
            remove(window);
            if (activeWindow == window) {
                activeWindow = null;
            }
            syncTaskBars();
        }
    }

    private void markActive(Window window) {
        if (activeWindow != null && activeWindow != window) {
            markInactive(activeWindow);
        }
        syncTaskBars();
        setMinimized(window, false);
    }

    private void markInactive(Window window) {
        if (window == activeWindow) {
            activeWindow = null;
            syncTaskBars();
        }
    }

    private void hideWindow(Window window) {
        if (getWindowDescription(window).isMinimizingNow()) {
            markInactive(window);
            getWindowDescription(window).setMinimizingNow(false);
            return;
        }
        removeWindow(window);
    }

    private void minimizeWindow(Window window) {
        setMinimized(window, true);
        getWindowDescription(window).setMinimizingNow(true);
        window.hide();
    }

    public void showWindow(Window window) {
        setMinimized(window, false);
        window.show();
        syncTaskBars();
    }

    public void registerTaskBar(TaskBar taskBar) {
        if (taskBars.contains(taskBar)) {
            throw new IllegalArgumentException("Task bar already registered");
        }
        taskBars.add(taskBar);
    }

    public void unregisterTaskBar(TaskBar taskBar) {
        if (!taskBars.contains(taskBar)) {
            throw new IllegalArgumentException("No such task bar registered");
        }
        taskBars.remove(taskBar);
    }

    private void syncTaskBars() {
        for (TaskBar b : taskBars) {
            b.syncWindows(windows.keySet());
            b.setActive(activeWindow);
        }
    }

    public Window getActive() {
        return activeWindow;
    }

    public List<WindowBuilder> getBuilders() {
        return new ArrayList<WindowBuilder>(builders.values());
    }

    public boolean containsWindow(Window w) {
        return windows.containsKey(w);
    }

    public Set<Window> getWindows() {
        return this.windows.keySet();
    }

    private class WindowHandler implements ActivateHandler<Window>,
        DeactivateHandler<Window>, MinimizeHandler, HideHandler,
        ShowHandler {

        @Override
        public void onActivate(ActivateEvent<Window> event) {
            markActive((Window) event.getSource());
        }

        @Override
        public void onDeactivate(DeactivateEvent<Window> event) {
            markInactive((Window) event.getSource());
        }

        @Override
        public void onHide(HideEvent event) {
            hideWindow((Window) event.getSource());
        }

        @Override
        public void onMinimize(MinimizeEvent event) {
            minimizeWindow((Window) event.getSource());
        }

        @Override
        public void onShow(ShowEvent event) {
            showWindow((Window) event.getSource());
        }
    }

    private class WindowDescription {

        private final List<HandlerRegistration> handlerRegistrations =
            new LinkedList<HandlerRegistration>();
        private boolean isMinimized;
        private boolean minimizingNow;

        public boolean isMinimized() {
            return isMinimized;
        }

        public void setMinimized(boolean minimized) {
            this.isMinimized = minimized;
        }

        public void addHandlerRegistration(HandlerRegistration registration) {
            handlerRegistrations.add(registration);
        }

        public void removeHandlerRegistrations() {
            for (HandlerRegistration handlerRegistration : handlerRegistrations) {
                handlerRegistration.removeHandler();
            }
            handlerRegistrations.clear();
        }

        public boolean isMinimizingNow() {
            return minimizingNow;
        }

        public void setMinimizingNow(boolean minimizingNow) {
            this.minimizingNow = minimizingNow;
        }
    }
}