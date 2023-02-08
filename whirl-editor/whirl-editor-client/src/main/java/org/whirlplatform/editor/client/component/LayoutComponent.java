package org.whirlplatform.editor.client.component;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.container.AbstractHtmlLayoutContainer.HtmlData;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor.DoublePropertyEditor;
import java.util.HashMap;
import java.util.Map;
import org.whirlplatform.meta.shared.component.PropertyType;
import org.whirlplatform.meta.shared.editor.PropertyValue;

public class LayoutComponent extends CenterLayoutContainer
    implements HasValueChangeHandlers<PropertyType> {

    private Map<PropertyType, PropertyValue> properties;
    private NumberField<Double> marginTop;
    private NumberField<Double> marginRight;
    private NumberField<Double> marginBottom;
    private NumberField<Double> marginLeft;
    private NumberField<Double> layoutWidth;
    private NumberField<Double> layoutHeight;
    private NumberField<Double> componentWidth;
    private NumberField<Double> componentHeight;

    public LayoutComponent() {
        properties = new HashMap<PropertyType, PropertyValue>();

        initUI();
    }

    private void initUI() {
        LayoutComponentTemplate tmpl = GWT.create(LayoutComponentTemplate.class);
        HtmlLayoutContainer container = new HtmlLayoutContainer(tmpl.getTemplate());
        setWidget(container);

        marginTop = new NumberField<Double>(new DoublePropertyEditor());
        marginTop.setWidth(40);
        addChangeHandler(marginTop, PropertyType.LayoutDataMarginTop);
        container.add(marginTop, new HtmlData(".margin-top"));

        marginRight = new NumberField<Double>(new DoublePropertyEditor());
        marginRight.setWidth(40);
        addChangeHandler(marginRight, PropertyType.LayoutDataMarginRight);
        container.add(marginRight, new HtmlData(".margin-right"));

        marginBottom = new NumberField<Double>(new DoublePropertyEditor());
        marginBottom.setWidth(40);
        addChangeHandler(marginBottom, PropertyType.LayoutDataMarginBottom);
        container.add(marginBottom, new HtmlData(".margin-bottom"));

        marginLeft = new NumberField<Double>(new DoublePropertyEditor());
        marginLeft.setWidth(40);
        addChangeHandler(marginLeft, PropertyType.LayoutDataMarginLeft);
        container.add(marginLeft, new HtmlData(".margin-left"));

        layoutWidth = new NumberField<Double>(new DoublePropertyEditor());
        layoutWidth.setWidth(40);
        addChangeHandler(layoutWidth, PropertyType.LayoutDataWidth);
        container.add(layoutWidth, new HtmlData(".layout-width"));

        layoutHeight = new NumberField<Double>(new DoublePropertyEditor());
        layoutHeight.setWidth(40);
        addChangeHandler(layoutHeight, PropertyType.LayoutDataHeight);
        container.add(layoutHeight, new HtmlData(".layout-height"));

        componentWidth = new NumberField<Double>(new DoublePropertyEditor());
        componentWidth.setWidth(40);
        addChangeHandler(componentWidth, PropertyType.Width);
        container.add(componentWidth, new HtmlData(".component-width"));

        componentHeight = new NumberField<Double>(new DoublePropertyEditor());
        componentHeight.setWidth(40);
        addChangeHandler(componentHeight, PropertyType.Height);
        container.add(componentHeight, new HtmlData(".component-height"));

        LayoutComponentStyle style = GWT.create(LayoutComponentStyle.class);
        style.getCss().ensureInjected();
    }

    public void setProperty(PropertyType type, PropertyValue value) {
        // Или записывать в properties даже null?
        if (value == null || value.getValue(null) == null || value.getValue(null).isNull()) {
            return;
        }
        properties.put(type, value);

        Double valueStr = value.getValue(null).getDouble();

        switch (type) {
            case LayoutDataMarginTop:
                marginTop.setValue(valueStr);
                break;
            case LayoutDataMarginRight:
                marginRight.setValue(valueStr);
                break;
            case LayoutDataMarginBottom:
                marginBottom.setValue(valueStr);
                break;
            case LayoutDataMarginLeft:
                marginLeft.setValue(valueStr);
                break;
            case LayoutDataWidth:
                layoutWidth.setValue(valueStr);
                break;
            case LayoutDataHeight:
                layoutHeight.setValue(valueStr);
                break;
            case Width:
                componentWidth.setValue(valueStr);
                break;
            case Height:
                componentHeight.setValue(valueStr);
                break;
            default:
        }
    }

    public Map<PropertyType, PropertyValue> getProperties() {
        return properties;
    }

    private void addChangeHandler(NumberField<Double> field, final PropertyType type) {
        field.addValueChangeHandler(new ValueChangeHandler<Double>() {

            @Override
            public void onValueChange(ValueChangeEvent<Double> event) {
                PropertyValue value = new PropertyValue(type.getType(), null, event.getValue());
                properties.put(type, value);
                ValueChangeEvent.fire(LayoutComponent.this, type);
            }
        });
    }

    public PropertyValue getProperty(PropertyType type) {
        return properties.get(type);
    }

    public void clearFields() {
        marginTop.clear();
        marginRight.clear();
        marginBottom.clear();
        marginLeft.clear();
        layoutWidth.clear();
        layoutHeight.clear();
        componentWidth.clear();
        componentHeight.clear();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<PropertyType> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public interface LayoutComponentTemplate extends XTemplates {
        @XTemplate(source = "LayoutComponent.html")
        SafeHtml getTemplate();
    }

    public interface LayoutComponentStyle extends ClientBundle {
        @Source("LayoutComponent.gss")
        CssResource getCss();
    }
}
