package org.whirlplatform.meta.shared.component;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.google.gwt.event.shared.GwtEvent.Type;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.whirlplatform.meta.shared.EventMetadata;
import org.whirlplatform.meta.shared.data.DataValue;

@SuppressWarnings("serial")
@JsonAutoDetect(fieldVisibility = Visibility.ANY, getterVisibility = Visibility.NONE,
    isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public class ComponentModel implements Cloneable, Serializable {

    protected List<ComponentModel> children = new ArrayList<ComponentModel>();
    protected Map<String, List<EventMetadata>> events = new HashMap<>();
    private String id;
    private ComponentType type;
    private Map<String, DataValue> values = new HashMap<>();
    private Set<String> replaceableProperties = new HashSet<String>();
    private List<ComponentModel> contextMenuItems = new ArrayList<ComponentModel>();

    public ComponentModel(ComponentType type) {
        this.type = type;
    }

    protected ComponentModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setValue(String name, DataValue value) {
        values.put(name, value);
    }

    public DataValue getValue(String name) {
        return values.get(name);
    }

    public boolean containsValue(String name) {
        return values.containsKey(name);
    }

    public boolean hasValues() {
        return !values.isEmpty();
    }

    public Map<String, DataValue> getValues() {
        return Collections.unmodifiableMap(values);
    }

    public void setValues(Map<String, DataValue> values) {
        this.values.clear();
        this.values.putAll(values);
    }

    public void addReplaceableProperty(String property) {
        replaceableProperties.add(property);
    }

    public Set<String> getReplaceableProperties() {
        return Collections.unmodifiableSet(replaceableProperties);
    }

    public void setReplaceableProperties(Set<String> properties) {
        replaceableProperties.clear();
        replaceableProperties.addAll(properties);
    }

    public boolean isReplaceableProperty(String property) {
        return replaceableProperties.contains(property);
    }

    public boolean removeReplaceableProperty(String property) {
        return replaceableProperties.remove(property);
    }

    public void addEvent(String handlerType, EventMetadata event) {
        List<EventMetadata> models = events.get(handlerType);
        if (models == null) {
            models = new ArrayList<EventMetadata>();
            events.put(handlerType, models);
        }
        models.add(event);
    }

    public void removeEvent(String handlerType, EventMetadata event) {
        List<EventMetadata> models = events.get(handlerType);
        if (models != null) {
            models.remove(event);
        }
    }

    public Set<String> getEventKeys() {
        return events.keySet();
    }

    public List<EventMetadata> getEventsByType(Type<?> handler) {
        List<EventMetadata> l = events.get(handler.toString());
        if (l == null) {
            l = Collections.emptyList();
        }
        return Collections.unmodifiableList(l);
    }

    public boolean hasEvents(Type<?> handler) {
        return events.containsKey(handler.toString());
    }

    public void addChild(ComponentModel child) {
        if (children == null) {
            children = new ArrayList<ComponentModel>();
        }
        children.add(child);
    }

    public List<? extends ComponentModel> getChildren() {
        ArrayList<ComponentModel> result = new ArrayList<ComponentModel>(children);
        Collections.sort(result, ModelChildComparator.COMPARATOR);
        return Collections.unmodifiableList(result);
    }

    public boolean hasChildren() {
        return children != null && children.size() >= 1;
    }

    public ComponentType getType() {
        return type;
    }

    public void setType(ComponentType type) {
        this.type = type;
    }

    public void addContextMenuItem(ComponentModel item) {
        contextMenuItems.add(item);
    }

    public void removeContextMenuItem(ComponentModel item) {
        contextMenuItems.remove(item);
    }

    public List<ComponentModel> getContextMenuItems() {
        return contextMenuItems;
    }

    public ComponentModel clone() {
        ComponentModel newComponent = new ComponentModel();
        newComponent.setId(id);
        newComponent.setType(getType());

        Map<String, DataValue> otherValues = new HashMap<String, DataValue>();
        for (String key : values.keySet()) {
            otherValues.put(key, values.get(key));
        }
        newComponent.setValues(otherValues);
        newComponent.setReplaceableProperties(replaceableProperties);

        for (Entry<String, List<EventMetadata>> h : events.entrySet()) {
            if (h.getValue() != null) {
                for (EventMetadata e : h.getValue()) {
                    newComponent.addEvent(h.getKey(), e.clone());
                }
            }
        }

        if (children != null) {
            for (ComponentModel c : children) {
                newComponent.addChild(c.clone());
            }
        }

        for (ComponentModel item : contextMenuItems) {
            newComponent.addContextMenuItem(item.clone());
        }
        return newComponent;
    }

    public void setChildren(List<ComponentModel> children) {
        this.children = children;
    }

    public Map<String, List<EventMetadata>> getEvents() {
        return events;
    }

    public void setEvents(Map<String, List<EventMetadata>> events) {
        this.events = events;
    }

    public void setContextMenuItems(List<ComponentModel> contextMenuItems) {
        this.contextMenuItems = contextMenuItems;
    }
}
