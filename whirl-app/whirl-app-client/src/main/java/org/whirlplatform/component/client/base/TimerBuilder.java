package org.whirlplatform.component.client.base;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import jsinterop.annotations.JsIgnore;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.event.TimeEvent;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

public class TimerBuilder extends ComponentBuilder implements TimeEvent.HasTimeHandlers {

    private int delay;
    private int period;

    private SimpleContainer container;

    public TimerBuilder(Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    public TimerBuilder() {
        this(Collections.emptyMap());
    }

    @Override
    public ComponentType getType() {
        return ComponentType.TimerType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        delay = 0;
        period = 0;
        container = new SimpleContainer();
        return container;
    }

    @JsIgnore
    @Override
    public Component create() {
        final Component component = super.create();
        Timer timer = new Timer() {
            @Override
            public void run() {
                Widget parent = component.getParent();
                if (parent == null) {
                    cancel();
                } else if (parent.isVisible()) {
                    TimerBuilder.this.ensureHandler().fireEvent(new TimeEvent());
                }
            }
        };
        if (delay > 0) {
            timer.schedule(delay);
        } else if (period > 0) {
            timer.scheduleRepeating(period);
        }
        return component;
    }

    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.Delay.getCode())) {
            if (value != null && value.getInteger() != null) {
                delay = value.getInteger();
            }
            return true;
        } else if (name.equalsIgnoreCase(PropertyType.Period.getCode())) {
            if (value != null && value.getInteger() != null) {
                period = value.getInteger();
            }
            return true;
        }
        return super.setProperty(name, value);
    }

    @Override
    protected <C> C getRealComponent() {
        return (C) container;
    }

    @Override
    public HandlerRegistration addTimeHandler(TimeEvent.TimeHandler handler) {
        return addHandler(handler, TimeEvent.getType());
    }

    @JsIgnore
    public void setIcon(ImageResource icon) {
        Image image = new Image(icon);
        container.add(image);
    }

    /**
     * Возвращает код компонента.
     *
     * @return код компонента
     */
    @Override
    public String getCode() {
        return super.getCode();
    }

    /**
     * Проверяет, находится ли компонент в скрытом состоянии.
     *
     * @return true, если компонент скрыт
     */
    @Override
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Устанавливает скрытое состояние компонента.
     *
     * @param hidden true - для скрытия компонента, false - для отображения компонента
     */
    @Override
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Фокусирует компонент.
     */
    @Override
    public void focus() {
        super.focus();
    }

    /**
     * Проверяет, включен ли компонент.
     *
     * @return true, если компонент включен
     */
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Устанавливает включенное состояние компонента.
     *
     * @param enabled true - для включения компонента, false - для отключения компонента
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }
}
