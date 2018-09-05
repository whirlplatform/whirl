package org.whirlplatform.editor.shared.templates;

import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.EventElement;

import java.io.Serializable;


@SuppressWarnings("serial")
public class BaseTemplate implements Serializable {

    private Type type;
    private AbstractElement element;

    @SuppressWarnings("unused")
    private BaseTemplate() {
    }

    public BaseTemplate(AbstractElement element) {
        this.element = element;
        if (element instanceof EventElement) {
            this.type = Type.EVENT_TEMPLATE;
        } else {
            this.type = Type.COMPONENT_TEMPLATE;
        }

    }

    public enum Type {
        COMPONENT_TEMPLATE,
        EVENT_TEMPLATE
    }

    public AbstractElement getElement() {
        return element;
    }

    public String getName() {
        return element.getName();
    }

    public void setName(String name) {
        element.setName(name);
    }

    /**
     * @return Тип шаблона
     */
    public Type getType() {
        return type;
    }
}
