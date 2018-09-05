package org.whirlplatform.meta.shared.editor;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class AbstractElement implements Serializable, ElementVisitable {

    protected String id;

    protected String name;

    public AbstractElement() {

    }

    public AbstractElement(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "[" + getName() + ":" + getId() + "]";
    }
}
	