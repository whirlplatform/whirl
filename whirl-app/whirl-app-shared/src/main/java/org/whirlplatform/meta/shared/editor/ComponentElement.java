package org.whirlplatform.meta.shared.editor;

import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.component.PropertyType;

import java.util.*;

@SuppressWarnings("serial")
public class ComponentElement extends AbstractElement {

    private ComponentType type;
    private Map<PropertyType, PropertyValue> values = new HashMap<PropertyType, PropertyValue>();

    private ComponentElement parent;

    private Set<ComponentElement> children = new HashSet<ComponentElement>();

    private Set<EventElement> events = new HashSet<EventElement>();

    private Set<ContextMenuItemElement> contextMenuItems = new HashSet<ContextMenuItemElement>();

    public ComponentElement() {
    }

    public ComponentElement(ComponentType type) {
        this.type = type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public ComponentType getType() {
        return type;
    }

    public Collection<ComponentElement> getChildren() {
        List<ComponentElement> result = new ArrayList<ComponentElement>(children);
        Collections.sort(result, ElementChildComparator.COMPARATOR);
        return Collections.unmodifiableList(result);
    }

    public void addChild(ComponentElement child) {
        if (child == null) {
            return;
        }
        if (child.parent != null) {
            child.parent.removeChild(child);
        }
        children.add(child);
        child.parent = this;
    }

    public void removeChild(ComponentElement element) {
        children.remove(element);
    }

    public boolean hasProperty(PropertyType name) {
        return values.containsKey(name);
    }

    public PropertyValue getProperty(PropertyType name) {
        PropertyValue result = values.get(name);
        if (result == null) {
            return new PropertyValue(name.getType());
        }
        return result;
    }

    public void setProperty(PropertyType name, PropertyValue value) {
        values.put(name, value);
    }

    public Map<PropertyType, PropertyValue> getProperties() {
        return Collections.unmodifiableMap(values);
    }

    protected void setParent(ComponentElement parent) {
        this.parent = parent;
    }

    public ComponentElement getParent() {
        return parent;
    }

    public void removeFromParent() {
        if (parent != null) {
            parent.removeChild(this);
        }
        parent = null;
    }

    public void addEvent(EventElement event) {
        events.add(event);
        event.setParentComponent(this);
    }

    public void removeEvent(EventElement event) {
        events.remove(event);
    }

    public Collection<EventElement> getEvents() {
        return Collections.unmodifiableSet(events);
    }

    public void removeProperty(PropertyType name) {
        values.remove(name);
    }

    public void addContextMenuItem(ContextMenuItemElement item) {
        if (item.getIndex() >= 0) {
            setContextMenuItemIndex(item, item.getIndex());
            item.setParentComponent(this);
        } else {
            item.setIndex(rebuildIndex() + 1);
            contextMenuItems.add(item);
            item.setParentComponent(this);
        }
    }

    public void removeContextMenuItem(ContextMenuItemElement item) {
        contextMenuItems.remove(item);
        rebuildIndex();
    }

    /**
     * Упорядоченный по индексам список элементов
     *
     * @return
     */
    public Collection<ContextMenuItemElement> getContextMenuItems() {
        List<ContextMenuItemElement> result = new ArrayList<ContextMenuItemElement>(contextMenuItems);
        Collections.sort(result, ContextMenuItemElement.getComparator());
        return Collections.unmodifiableList(result);
    }

    public boolean hasMenuItem(ContextMenuItemElement item) {
        return contextMenuItems.contains(item);
    }

    public void setContextMenuItemIndex(ContextMenuItemElement item, int index) {
        boolean addLast = index == contextMenuItems.size();
        int i = 0;
        contextMenuItems.remove(item);
        for (ContextMenuItemElement p : getContextMenuItems()) {
            if (p == item) {
                continue;
            }
            if (i == index) {
                i++;
            }
            p.setIndex(i);
            contextMenuItems.add(p);
            i++;
        }
        if (addLast) {
            item.setIndex(i);
        } else {
            item.setIndex(index);
        }
        contextMenuItems.add(item);
        item.setParentComponent(this);
    }

    private int rebuildIndex() {
        int index = 0;
        for (ContextMenuItemElement i : getContextMenuItems()) {
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
