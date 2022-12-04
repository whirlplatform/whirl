package org.whirlplatform.component.client.base;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.FrameElement;
import com.google.gwt.user.client.ui.Frame;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.WidgetComponent;
import java.util.Collections;
import java.util.Map;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

/**
 * Фрейм
 */
@JsType(name = "Frame", namespace = "Whirl")
public class FrameBuilder extends ComponentBuilder {

    private Frame frame;

    private WidgetComponent wrapper;

    private String template;

    @JsConstructor
    public FrameBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public FrameBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.FrameType;
    }

    @Override
    protected Component init(Map<String, DataValue> builderProperties) {
        frame = new Frame();
        wrapper = new WidgetComponent(frame);
        return wrapper;
    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.Template.getCode())) {
            if (value != null && value.getString() != null) {
                setTemplate(value.getString());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.Url.getCode())) {
            if (value != null && value.getString() != null) {
                frame.setUrl(value.getString());
                return true;
            }
        }
        return super.setProperty(name, value);
    }

    private void setTemplate(String template) {
        this.template = template;
        if (template != null) {
            FrameElement frameEl = frame.getElement().cast();
            BodyElement bodyEl = frameEl.getContentDocument().getBody();
            bodyEl.setInnerHTML(template);
        }
    }

    @Override
    protected <C> C getRealComponent() {
        return (C) frame;
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
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Устанавливает скрытое состояние компонента.
     *
     * @param hidden true - для скрытия компонента, false - для отображения компонента
     */
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Устанавливает фокус на компоненте.
     */
    @Override
    public void focus() {
        super.focus();
    }

    /**
     * Проверяет, включен ли компонент.
     *
     * @return true если компонент включен
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
