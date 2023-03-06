package org.whirlplatform.meta.shared.editor;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Data;

@SuppressWarnings("serial")
@Data
public class RightCollectionElement extends AbstractElement {

    private AbstractElement element;
    private Set<RightElement> applicationRights = new HashSet<RightElement>();
    private Map<GroupElement, Set<RightElement>> groupRights =
        new HashMap<GroupElement, Set<RightElement>>();

    public RightCollectionElement() {
    }

    public RightCollectionElement(AbstractElement element) {
        this.element = element;
    }

    public AbstractElement getElement() {
        return element;
    }

    @Override
    public String getName() {
        return element.getName();
    }

    public void addApplicationRight(RightElement right) {
        for (RightElement r : applicationRights) {
            if (r.getType().equals(right.getType())) {
                applicationRights.remove(r);
                break;
            }
        }
        applicationRights.add(right);
    }

    void removeApplicationRight(RightElement right) {
        applicationRights.remove(right);
    }

    public Collection<RightElement> getApplicationRights() {
        return Collections.unmodifiableSet(applicationRights);
    }

    public void addGroupRight(GroupElement group, RightElement right) {
        Set<RightElement> set = groupRights.get(group);
        if (set == null) {
            set = new HashSet<RightElement>();
            groupRights.put(group, set);
        }

        for (RightElement r : set) {
            if (r.getType().equals(right.getType())) {
                set.remove(r);
                break;
            }
        }
        set.add(right);
    }

    void removeGroupRight(GroupElement group, RightElement right) {
        Set<RightElement> set = groupRights.get(group);
        if (set != null) {
            set.remove(right);
            if (set.size() == 0) {
                groupRights.remove(group);
            }
        }
    }

    void removeGroupRights(GroupElement group) {
        groupRights.remove(group);
    }

    public Collection<RightElement> getGroupRights(GroupElement group) {
        if (groupRights.containsKey(group)) {
            return Collections.unmodifiableCollection(groupRights.get(group));
        } else {
            return Collections.emptySet();
        }
    }

    public Collection<RightElement> getAllGroupRights() {
        Set<RightElement> result = new HashSet<RightElement>();
        for (Set<RightElement> s : groupRights.values()) {
            result.addAll(s);
        }
        return Collections.unmodifiableSet(result);
    }

    public boolean isEmpty() {
        return applicationRights.isEmpty() && groupRights.isEmpty();
    }

    public void clear() {
        applicationRights.clear();
        groupRights.clear();
    }

    @Override
    public <T extends ElementVisitor.VisitContext> void accept(T ctx, ElementVisitor<T> visitor) {
        visitor.visit(ctx, this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((applicationRights == null) ? 0 : applicationRights.hashCode());
        result = prime * result + ((element == null) ? 0 : element.hashCode());
        result = prime * result + ((groupRights == null) ? 0 : groupRights.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (getClass() != obj.getClass()) {
            return false;
        }
        RightCollectionElement other = (RightCollectionElement) obj;
        if (!other.element.getId().equals(this.element.getId())) {
            return false;
        }
        if (other.applicationRights.size() != this.applicationRights.size()) {
            return false;
        }
        if (other.groupRights.keySet().size() != this.groupRights.keySet().size()) {
            return false;
        }
        if (!other.applicationRights.containsAll(this.applicationRights)) {
            return false;
        }
        if (!other.groupRights.keySet().containsAll(this.groupRights.keySet())) {
            return false;
        }
        for (GroupElement group : this.groupRights.keySet()) {
            Set<RightElement> otherRights = other.groupRights.get(group);
            Set<RightElement> thisRights = this.groupRights.get(group);
            if (otherRights.size() != thisRights.size()) {
                return false;
            }
            if (!(otherRights.containsAll(thisRights))) {
                return false;
            }
        }
        return true;
    }
}
