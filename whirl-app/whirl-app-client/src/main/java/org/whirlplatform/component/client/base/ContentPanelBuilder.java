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

    @Override
    public void clearContainer() {
        if (topComponent != null) {
            removeChild(topComponent);
        }
    }

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
     * Returns component's code.
     *
     * @return component's code
     */
    @Override
    public String getCode() {
        return super.getCode();
    }

    /**
     * Checks if component is in hidden state.
     *
     * @return true if component is hidden
     */
    @Override
    public boolean isHidden() {
        return super.isHidden();
    }

    /**
     * Sets component's hidden state.
     *
     * @param hidden true - to hide component, false - to show component
     */
    @Override
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
    }

    /**
     * Focuses component.
     */
    @Override
    public void focus() {
        super.focus();
    }

    /**
     * Checks if component is enabled.
     *
     * @return true if component is enabled
     */
    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    /**
     * Sets component's enabled state.
     *
     * @param enabled true - to enable component, false - to disable component
     */
    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

}
