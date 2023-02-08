package org.whirlplatform.editor.client.tree.part;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.whirlplatform.editor.client.meta.NewComponentElement;
import org.whirlplatform.editor.client.meta.NewContextMenuItemElement;
import org.whirlplatform.editor.client.meta.NewEventElement;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.editor.client.tree.dummy.DummyComponentEvents;
import org.whirlplatform.editor.client.tree.dummy.DummyMenuItems;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.ContextMenuItemElement;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.meta.shared.editor.RightType;

public class AppTreeComponentPart extends AbstractAppTreePart<ComponentElement> {

    private ComponentFolders folders;

    AppTreeComponentPart(AppTree appTree, AppTreePresenter treePresenter,
                         ComponentElement component) {
        super(appTree, treePresenter, component);
        folders = new ComponentFolders(component);
    }

    @Override
    public void init() {
        for (ComponentElement c : handledElement.getChildren()) {
            treePresenter.riseAddElementUI(handledElement, c);
        }

        addChildElement(handledElement, folders.eventFolder);
        for (EventElement e : handledElement.getEvents()) {
            treePresenter.riseAddElementUI(handledElement, e);
        }

        addChildElement(handledElement, folders.menuItemFolder);
        for (ContextMenuItemElement e : handledElement.getContextMenuItems()) {
            treePresenter.riseAddElementUI(handledElement, e);
        }
    }

    @Override
    public boolean isRenaming(AbstractElement element) {
        return handledElement == element;
    }

    @Override
    public boolean isAdding(AbstractElement element) {
        return element == folders.eventFolder || element == folders.menuItemFolder;
    }

    @Override
    public boolean isCopying(AbstractElement element) {
        return element instanceof ComponentElement;
    }

    @Override
    public boolean isPasting(AbstractElement parent, AbstractElement element) {
        if ((parent == folders.eventFolder && element instanceof EventElement)
            || (parent == handledElement && element instanceof EventElement)) {
            return true;
        } else {
            return parent == folders.menuItemFolder && element instanceof ContextMenuItemElement;
        }
    }

    @Override
    public boolean isEditing(AbstractElement element) {
        return element == handledElement;
    }

    @Override
    public boolean isDeleting(AbstractElement element) {
        return (element instanceof ComponentElement
            && handledElement.getChildren().contains(element))
            || (element instanceof EventElement && handledElement.getEvents().contains(element));
    }

    @Override
    public boolean hasRights(AbstractElement element) {
        return element == handledElement || element == folders.eventFolder;
    }

    @Override
    public boolean doAddElement(AbstractElement parent, AbstractElement element) {
        if (parent == folders.eventFolder && element == null) {
            treePresenter.riseAddElement(handledElement, new NewEventElement());
            return true;
        } else if ((parent == handledElement || parent == folders.eventFolder)
            && element instanceof EventElement) {
            treePresenter.riseAddElement(handledElement, element);
            return true;
        } else if (parent == folders.menuItemFolder && element == null) {
            treePresenter.riseAddElement(handledElement, new NewContextMenuItemElement());
            return true;
        } else if (parent instanceof ComponentElement) {
            ComponentType ct = ((ComponentElement) parent).getType();
            if (ct == ComponentType.HorizontalMenuType || ct == ComponentType.TreeMenuType
                || ct == ComponentType.HorizontalMenuItemType) {
                // добавление меню
                treePresenter.riseAddElement(parent,
                    new NewComponentElement(ComponentType.HorizontalMenuItemType));

                // treeHandler.getEventBus().addElement(element,
                // new NewPropertyElement(PropertyType.LayoutDataIndex, null,
                // ((ComponentElement) parent).getChildren().size()));

                treePresenter.riseOpenElement(parent);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean doAddElementUI(AbstractElement parent, AbstractElement element) {
        if (parent == handledElement && handledElement.getChildren().contains(element)) {
            removeElement(element);
            addChildElement(handledElement, element);
            putTreePart(element,
                new AppTreeComponentPart(appTree, treePresenter, (ComponentElement) element));
            return true;
        } else if (parent == handledElement && element instanceof EventElement) {
            removeElement(element);
            addChildElement(folders.eventFolder, element);
            putTreePart(element,
                new AppTreeEventPart(appTree, treePresenter, (EventElement) element));
            return true;
        } else if (parent == handledElement && element instanceof ContextMenuItemElement) {
            removeElement(element);
            addChildElement(folders.menuItemFolder, element);
            putTreePart(element, new AppTreeContextMenuPart(appTree, treePresenter,
                (ContextMenuItemElement) element));
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElement(AbstractElement parent, AbstractElement element) {
        if ((element instanceof ComponentElement && handledElement.getChildren().contains(element))
            || (element instanceof EventElement && handledElement.getEvents().contains(element))
            || (element instanceof ContextMenuItemElement
            && handledElement.hasMenuItem((ContextMenuItemElement) element))) {
            treePresenter.riseRemoveElement(handledElement, element, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElementUI(AbstractElement parent, AbstractElement element) {
        if ((element instanceof ComponentElement
            && appTree.hasChild(handledElement, element, false))
            || (element instanceof EventElement
            && appTree.hasChild(folders.eventFolder, element, false))
            || (element instanceof ContextMenuItemElement
            && appTree.hasChild(folders.menuItemFolder, element, false))) {
            removeElement(element);
            return true;
        }
        return false;
    }

    @Override
    public boolean doEditElement(AbstractElement element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean doEditElementRights(AbstractElement element) {
        if (element == handledElement) {
            Collection<EventElement> events = getAllComponentsEvents(handledElement);
            if (!events.isEmpty()) {
                treePresenter.riseEditRights(events, Collections.singleton(RightType.EXECUTE));
            }
            return true;
        } else if (element == folders.eventFolder) {
            Collection<EventElement> events = handledElement.getEvents();
            if (!events.isEmpty()) {
                treePresenter.riseEditRights(events, Collections.singleton(RightType.EXECUTE));
            }
            return true;
        }
        return false;
    }

    // TODO дублируется
    private Collection<EventElement> getAllComponentsEvents(ComponentElement component) {
        Set<EventElement> result = new HashSet<EventElement>();
        result.addAll(component.getEvents());
        for (ComponentElement c : component.getChildren()) {
            result.addAll(getAllComponentsEvents(c));
        }
        return result;
    }

    @Override
    public boolean canDragDrop(AbstractElement dropTarget, Object dropData) {
        if ((dropTarget == folders.eventFolder || dropTarget == handledElement)
            && dropData instanceof EventElement) {
            return true;
        } else {
            return dropData instanceof ContextMenuItemElement
                && handledElement.hasMenuItem((ContextMenuItemElement) dropData)
                && (dropTarget == folders.menuItemFolder
                || (dropTarget instanceof ContextMenuItemElement
                && handledElement.hasMenuItem((ContextMenuItemElement) dropTarget)));
        }
    }

    @Override
    public boolean doDragDrop(AbstractElement dropTarget, Object dropData) {
        if ((dropTarget == folders.eventFolder || dropTarget == handledElement)
            && dropData instanceof EventElement) {
            treePresenter.riseAddElement(handledElement, (EventElement) dropData);
            return true;
        } else if (dropTarget instanceof ContextMenuItemElement
            && handledElement.hasMenuItem((ContextMenuItemElement) dropData)
            && handledElement.hasMenuItem((ContextMenuItemElement) dropTarget)) {
            ContextMenuItemElement item = (ContextMenuItemElement) dropData;
            item.setIndex(((ContextMenuItemElement) dropTarget).getIndex());
            treePresenter.riseAddElement(handledElement, item);
            appTree.setExpanded(folders.menuItemFolder, true);
            return true;
        } else if (dropTarget == folders.menuItemFolder
            && dropData instanceof ContextMenuItemElement
            && handledElement.hasMenuItem((ContextMenuItemElement) dropData)) {
            ContextMenuItemElement item = (ContextMenuItemElement) dropData;
            item.setIndex(handledElement.getContextMenuItems().size());
            treePresenter.riseAddElement(handledElement, item);

            appTree.setExpanded(folders.menuItemFolder, true);
            return true;
        }
        return false;
    }

    private class ComponentFolders {
        private DummyComponentEvents eventFolder;
        private DummyMenuItems menuItemFolder;

        private ComponentFolders(ComponentElement component) {
            final String componentId = component.getId();
            eventFolder = new DummyComponentEvents(componentId);
            menuItemFolder = new DummyMenuItems(componentId);
        }
    }
}
