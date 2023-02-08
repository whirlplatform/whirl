package org.whirlplatform.editor.client.presenter.compare;

import com.sencha.gxt.data.shared.TreeStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.whirlplatform.editor.client.tree.visitor.ChangesSorterVisitContext;
import org.whirlplatform.editor.client.tree.visitor.ChangesSorterVisitor;
import org.whirlplatform.editor.client.tree.visitor.VisitableTreeElement;
import org.whirlplatform.editor.shared.merge.ChangeUnit;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.LocaleElement;
import org.whirlplatform.meta.shared.editor.RightCollectionElement;

/**
 * Сортирует полученные от diff изменения по соответствующим абстрактным элементам приложения для
 * отображения в дереве
 */
public class ChangesSorter {
    private ChangesSorterVisitContext ctx;
    private ChangesSorterVisitor visitor;
    private Map<String, ChangesSorterEntry> changesMap;

    public ChangesSorter() {
        changesMap = new HashMap<>();
        ctx = new ChangesSorterVisitContext();
        visitor = new ChangesSorterVisitor();
    }

    public ChangesSorter(TreeStore<AbstractElement> treeStore) {
        this();
        this.setTreeStore(treeStore);
    }

    public void setTreeStore(TreeStore<AbstractElement> treeStore) {
        ctx.setTreeStore(treeStore);
    }

    public boolean changesExist(final String id) {
        return changesMap.containsKey(id);
    }

    public List<ChangeUnit> getChangeUnits(final String elementId) {
        return (changesExist(elementId)) ? changesMap.get(elementId).getChangeUnits() :
            new ArrayList<ChangeUnit>();
    }

    public ElementChangeState getChangeState(final String elementId) {
        return (changesExist(elementId)) ? changesMap.get(elementId).getChangeState() :
            ElementChangeState.NONE;
    }

    public void clear() {
        changesMap.clear();
    }

    public void handleChanges(List<ChangeUnit> changes) {
        clear();
        ctx.init();
        sort(changes);
    }

    private void sort(List<ChangeUnit> changes) {
        for (ChangeUnit changeUnit : changes) {
            Object rightValue = changeUnit.getRightValue();
            Object leftValue = changeUnit.getLeftValue();
            if (rightValue == null && leftValue == null) {
                continue;
            }
            switch (changeUnit.getType()) {
                case Add:
                    processAddedOrRemovedObject(rightValue, changeUnit, ElementChangeState.ADDED);
                    break;
                case Change:
                    if (rightValue instanceof RightCollectionElement) {
                        if (changeUnit.getKey() instanceof AbstractElement) {
                            AbstractElement parent = (AbstractElement) changeUnit.getKey();
                            putChangedTarget(parent, changeUnit);
                        }
                    } else {
                        putChangedTarget(changeUnit.getTarget(), changeUnit);
                    }
                    break;
                case Remove:
                    processAddedOrRemovedObject(leftValue, changeUnit, ElementChangeState.REMOVED);
                    break;
                default:
            }
        }
    }

    private void processAddedOrRemovedObject(Object value, ChangeUnit unit,
                                             ElementChangeState changeState) {
        if (value instanceof LocaleElement) {
            putChange(ctx.getDummyLocalesId(), unit, ElementChangeState.CHANGED);
        } else if (value instanceof ApplicationElement) {
            putChange(((ApplicationElement) value).getId(), unit, changeState);
        } else if (value instanceof AbstractElement) {
            putAddedOrRemovedElement((AbstractElement) value, unit, changeState);
        } else {
            putChangedTarget(unit.getTarget(), unit);
        }
    }

    private void putChangedTarget(final AbstractElement element, final ChangeUnit unit) {
        String ownerId = findOwnerId(element);
        if (ownerId != null) {
            putChange(ownerId, unit, ElementChangeState.CHANGED);
        }
    }

    private void putAddedOrRemovedElement(final AbstractElement element, final ChangeUnit unit,
                                          ElementChangeState state) {
        String ownerId = findOwnerId(element);
        if (ownerId != null) {
            if (element.getId().equals(ownerId)) {
                putChange(element.getId(), unit, state);
                putInheritedChildren(element, state);
            } else {
                putChange(ownerId, unit, ElementChangeState.CHANGED);
            }
        }
    }

    private void putInheritedChildren(AbstractElement element, ElementChangeState state) {
        // AbstractElement parent =
        // getStore().findModelWithKey(element.getId());
        // if (parent != null) {
        // List<AbstractElement> list = getStore().getAllChildren(parent);
        // for (AbstractElement child : list) {
        // if (!(child instanceof TreeDummy)) {
        // changeStates.put(child.getId(), state);
        // }
        // }
        // }
    }

    private void putChange(final String id, final ChangeUnit unit, final ElementChangeState state) {
        if (id != null) {
            if (!changesExist(id)) {
                changesMap.put(id, new ChangesSorterEntry());
            }
            ChangesSorterEntry entry = changesMap.get(id);
            entry.addChangeUnit(unit);
            entry.setChangeState(state);
        }
    }

    private String findOwnerId(final AbstractElement element) {
        if (element instanceof VisitableTreeElement) {
            ((VisitableTreeElement) element).accept(ctx, visitor);
        } else {
            element.accept(ctx, visitor);
        }
        return ctx.getChangeOwnerId();
    }
}
