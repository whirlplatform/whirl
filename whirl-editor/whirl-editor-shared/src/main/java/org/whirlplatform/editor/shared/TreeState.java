package org.whirlplatform.editor.shared;

import org.whirlplatform.meta.shared.editor.AbstractElement;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("serial")
public class TreeState implements Serializable {

    private AbstractElement selected;
    private List<AbstractElement> expanded = new ArrayList<AbstractElement>();

    public TreeState() {
    }

    public void setSelected(AbstractElement selected) {
        this.selected = selected;
    }

    public AbstractElement getSelected() {
        return selected;
    }

    public void addExpanded(AbstractElement element) {
        expanded.add(element);
    }

    public List<AbstractElement> getExpanded() {
        return Collections.unmodifiableList(expanded);
    }
}
