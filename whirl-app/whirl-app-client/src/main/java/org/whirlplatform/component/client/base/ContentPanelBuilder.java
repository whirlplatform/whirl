package org.whirlplatform.component.client.base;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import jsinterop.annotations.JsConstructor;
import jsinterop.annotations.JsIgnore;
import jsinterop.annotations.JsOptional;
import jsinterop.annotations.JsType;
import org.whirlplatform.component.client.ComponentBuilder;
import org.whirlplatform.component.client.Containable;
import org.whirlplatform.component.client.TitleProvider;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.data.DataValue;

import java.util.Collections;
import java.util.Map;

/**
 * Контент-панель
 */
@JsType(name = "ContentPanel", namespace = "Whirl")
public class ContentPanelBuilder extends ComponentBuilder implements Containable {

    private ContentPanel panel;

    private ComponentBuilder topComponent;

    @JsConstructor
    public ContentPanelBuilder(@JsOptional Map<String, DataValue> builderProperties) {
        super(builderProperties);
    }

    @JsIgnore
    public ContentPanelBuilder() {
        this(Collections.emptyMap());
    }

    @JsIgnore
    @Override
    public ComponentType getType() {
        return ComponentType.ContentPanelType;
    }

    protected Component init(Map<String, DataValue> builderProperties) {
        panel = new ContentPanel();
        return panel;
    }

    @Override
    protected void setInitProperties(Map<String, DataValue> properties) {
        super.setInitProperties(properties);

        if (panel.getHeader().getHTML() == null) {
            panel.setHeaderVisible(false);
        }
        panel.setBodyBorder(false);
    }

    @JsIgnore
    @Override
    public boolean setProperty(String name, DataValue value) {
        if (name.equalsIgnoreCase(PropertyType.Title.getCode())) {
            if (value != null) {
                setHeadingHtml(value.getString());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.Html.getCode())) {
            if (value != null && value.getString() != null) {
                setHtmlContent(value.getString());
                return true;
            }
        } else if (name.equalsIgnoreCase(PropertyType.BodyStyle.getCode())) {
            if (value != null && value.getString() != null) {
                setBodyStyle(value.getString());
                return true;
            }
        }
        return super.setProperty(name, value);
    }

    private void setHeadingHtml(String title) {
        if (title != null) {
            panel.setHeaderVisible(true);
            panel.setHeading(SafeHtmlUtils.fromTrustedString(title));
        } else {
            panel.setHeaderVisible(false);
        }
    }

    private void setHtmlContent(String text) {
        panel.setWidget(new HTML(text));
    }

    private void setBodyStyle(String style) {
        panel.setBodyStyle(style);
    }

    @Override
    public void addChild(ComponentBuilder child) {
        panel.add(child.getComponent());
        if (getTitle() == null || getTitle().isEmpty() && child instanceof TitleProvider) {
            setHeadingHtml(child.getTitle());
        }

        topComponent = child;
        child.setParentBuilder(this);
    }

    @Override
    public void removeChild(ComponentBuilder child) {
        if (panel.remove(child.getComponent())) {
            topComponent.setParentBuilder(null);
            topComponent = null;
        }
    }

    /**
     * Очищает контейнер.
     */
    @Override
    public void clearContainer() {
        if (topComponent != null) {
            removeChild(topComponent);
        }
    }

    /**
     * Пересчитывает расположение компонентов в данном контейнере.
     */
    @Override
    public void forceLayout() {
        panel.forceLayout();
    }

    @Override
    protected <C> C getRealComponent() {
        return (C) panel;
    }

    @Override
    public ComponentBuilder[] getChildren() {
        if (topComponent != null) {
            ComponentBuilder[] result = {topComponent};
            return result;
        } else {
            return new ComponentBuilder[0];
        }
    }

    @Override
    public int getChildrenCount() {
        if (topComponent == null) {
            return 0;
        }
        return 1;
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
     * Фокусирует компонент.
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
     * @param enabled true - для включения компонента,
     *                false - для отключения компонента
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

}
