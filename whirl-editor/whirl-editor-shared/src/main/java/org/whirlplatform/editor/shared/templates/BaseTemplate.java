package org.whirlplatform.editor.shared.templates;

import java.io.Serializable;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.EventElement;


@SuppressWarnings("serial")
public class BaseTemplate implements Serializable {

    private Type type;
    private AbstractElement element;
    private boolean editable;

    @SuppressWarnings("unused")
    private BaseTemplate() {
    }

    public BaseTemplate(AbstractElement element, boolean editable) {
        this.element = element;
        if (element instanceof EventElement) {
            this.type = Type.EVENT_TEMPLATE;
        } else {
            this.type = Type.COMPONENT_TEMPLATE;
        }
        this.editable = editable;
    }

    public boolean isEditable() {
        return editable;
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

    public enum Type {
        COMPONENT_TEMPLATE("component"),
        EVENT_TEMPLATE("event");

        String path;

        Type(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}
