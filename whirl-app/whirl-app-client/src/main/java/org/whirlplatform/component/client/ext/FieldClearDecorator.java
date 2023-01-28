package org.whirlplatform.component.client.ext;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.DomQuery;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;

public class FieldClearDecorator {

    private Element crossEl;
    private ValueBaseField<?> field;
    private Command command;
    private HandlerRegistration registration;

    public FieldClearDecorator(ValueBaseField<?> field, Command clearCommand) {
        this.field = field;
        this.command = clearCommand;

        ClearDecoratorTemplate template = GWT
                .create(ClearDecoratorTemplate.class);
        ClearDecoratorResources resource = GWT
                .create(ClearDecoratorResources.class);
        resource.style().ensureInjected();
        SafeHtml str = template.render(resource.style());
        crossEl = XDOM.create(str);
        crossEl.setAttribute("src", resource.cross().getSafeUri().asString());

        init();
    }

    public void setRightOffset(double value) {
        if (crossEl != null) {
            crossEl.getStyle().setRight(value, Unit.PX);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void init() {
        if (field.getValue() != null && field.getText() != null) {
            show();
        }
        Event.sinkEvents(crossEl, Event.ONCLICK);
        Event.setEventListener(crossEl, new EventListener() {

            @Override
            public void onBrowserEvent(Event event) {
                if (event.getTypeInt() == Event.ONCLICK) {
                    clear();
                }
            }
        });
        registration = field.addValueChangeHandler(new ValueChangeHandler() {
            @Override
            public void onValueChange(ValueChangeEvent event) {
                if (event.getValue() != null) {
                    show();
                } else {
                    hide();
                }
            }
        });
    }

    private void clear() {
        if (field.isEnabled() && !field.isReadOnly()) {
            command.execute();
        }
    }

    public void show() {
        Element input = DomQuery.selectNode("input", field.getElement());
        Element parent = input.getParentElement();
        parent.insertAfter(crossEl, input);
    }

    public void hide() {
        crossEl.removeFromParent();
    }

    public void remove() {
        registration.removeHandler();
        field = null;
    }

    public Element getElement() {
        return crossEl;
    }

    public interface ClearDecoratorResources extends ClientBundle {

        @Source("clear-cross.png")
        ImageResource cross();

        @Source("ClearDecorator.css")
        ClearDecoratorStyle style();

    }

    public interface ClearDecoratorStyle extends CssResource {

        String clear();

    }

    public interface ClearDecoratorTemplate extends XTemplates {

        @XTemplate(source = "ClearDecorator.html")
        SafeHtml render(ClearDecoratorStyle style);

    }
}
