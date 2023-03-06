package org.whirlplatform.meta.shared.editor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Data;
import org.whirlplatform.meta.shared.data.DataType;

@SuppressWarnings("serial")
@Data
public class ContextMenuItemElement extends AbstractElement {

    // Нет смысла каждый раз создавать новый
    private static final Comparator<ContextMenuItemElement> comparator =
        new Comparator<ContextMenuItemElement>() {
            @Override
            public int compare(ContextMenuItemElement o1, ContextMenuItemElement o2) {
                int ind1 = o1 == null ? -1 : o1.getIndex();
                int ind2 = o2 == null ? -1 : o2.getIndex();
                return ind1 - ind2;
            }
        };

    private Set<EventElement> events = new HashSet<EventElement>();
    private ComponentElement parentComponent;
    private Set<ContextMenuItemElement> children = new HashSet<ContextMenuItemElement>();

    private int index = -1;
    private PropertyValue label = new PropertyValue(DataType.STRING);
    private String imageUrl;

    public ContextMenuItemElement() {
    }

    public static Comparator<ContextMenuItemElement> getComparator() {
        return comparator;
    }

    public void setParentComponent(ComponentElement parentComponent) {
        this.parentComponent = parentComponent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void addEvent(EventElement event) {
        events.add(event);
        event.setParentComponent(parentComponent);
    }

    public void removeEvent(EventElement event) {
        events.remove(event);
    }

    public Collection<EventElement> getEvents() {
        return Collections.unmodifiableSet(events);
    }

    public PropertyValue getLabel() {
        return label;
    }

    public void setLabel(PropertyValue label) {
        this.label = label;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addChild(ContextMenuItemElement item) {
        if (item.getIndex() >= 0) {
            setChildIndex(item, item.getIndex());
            item.setParentComponent(parentComponent);
        } else {
            item.setIndex(rebuildIndex() + 1);
            children.add(item);
            item.setParentComponent(parentComponent);
        }
    }

    public void removeChild(ContextMenuItemElement item) {
        children.remove(item);
        rebuildIndex();
    }

    /**
     * Упорядоченный по индексам список элементов
     *
     * @return
     */
    public Collection<ContextMenuItemElement> getChildren() {
        List<ContextMenuItemElement> result = new ArrayList<ContextMenuItemElement>(children);
        Collections.sort(result, comparator);
        return Collections.unmodifiableList(result);
    }

    public boolean hasChild(ContextMenuItemElement item) {
        return children.contains(item);
    }

    public void setChildIndex(ContextMenuItemElement item, int index) {
        boolean addLast = index == children.size();
        int i = 0;
        children.remove(item);
        for (ContextMenuItemElement p : getChildren()) {
            if (p == item) {
                continue;
            }
            if (i == index) {
                i++;
            }
            p.setIndex(i);
            children.add(p);
            i++;
        }
        if (addLast) {
            item.setIndex(i);
        } else {
            item.setIndex(index);
        }
        children.add(item);
        item.setParentComponent(parentComponent);
    }

    private int rebuildIndex() {
        int index = 0;
        for (ContextMenuItemElement i : getChildren()) {
            i.setIndex(index);
            index++;
        }
        return index - 1;
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }
}
