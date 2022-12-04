package org.whirlplatform.editor.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.whirlplatform.meta.shared.editor.AbstractElement;

@SuppressWarnings("serial")
public class TreeState implements Serializable {

    private AbstractElement selected;
    private List<AbstractElement> expanded = new ArrayList<AbstractElement>();

    public TreeState() {
    }

    public AbstractElement getSelected() {
        return selected;
    }

    public void setSelected(AbstractElement selected) {
        this.selected = selected;
    }

    public void addExpanded(AbstractElement element) {
        expanded.add(element);
    }

    public List<AbstractElement> getExpanded() {
        return Collections.unmodifiableList(expanded);
    }
}
