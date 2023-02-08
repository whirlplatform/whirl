package org.whirlplatform.editor.client.tree.part;

import java.util.Collections;
import org.whirlplatform.editor.client.meta.NewContextMenuItemElement;
import org.whirlplatform.editor.client.meta.NewEventElement;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.editor.client.tree.dummy.DummyMenuItemEvents;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ContextMenuItemElement;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.meta.shared.editor.RightType;

public class AppTreeContextMenuPart extends AbstractAppTreePart<ContextMenuItemElement> {

    private ContextMenuFolders folders;

    public AppTreeContextMenuPart(AppTree appTree, AppTreePresenter treePresenter,
                                  ContextMenuItemElement item) {
        super(appTree, treePresenter, item);
        folders = new ContextMenuFolders();
    }

    @Override
    public void init() {
        for (ContextMenuItemElement e : handledElement.getChildren()) {
            treePresenter.riseAddElementUI(handledElement, e);
        }
        addChildElement(handledElement, folders.eventFolder);
        for (EventElement e : handledElement.getEvents()) {
            treePresenter.riseAddElementUI(handledElement, e);
        }
    }

    @Override
    public boolean isRenaming(AbstractElement element) {
        return element == handledElement;
    }

    @Override
    public boolean isAdding(AbstractElement element) {
        return element == folders.eventFolder || element == handledElement;
    }

    @Override
    public boolean isEditing(AbstractElement element) {
        return element == handledElement;
    }

    @Override
    public boolean isDeleting(AbstractElement element) {
        return element == handledElement;
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
        } else if (parent == folders.eventFolder && element instanceof EventElement) {
            treePresenter.riseAddElement(handledElement, element);
            return true;
        } else if (parent == handledElement && element == null) {
            treePresenter.riseAddElement(handledElement, new NewContextMenuItemElement());
            return true;
        }
        return false;
    }

    @Override
    public boolean doAddElementUI(AbstractElement parent, AbstractElement element) {
        if (parent == handledElement && element instanceof EventElement) {
            removeElement(element);
            addChildElement(folders.eventFolder, element);
            putTreePart(element,
                new AppTreeEventPart(appTree, treePresenter, (EventElement) element));
            return true;
        } else if (parent == handledElement && element instanceof ContextMenuItemElement) {
            removeElement(element);
            addChildElement(handledElement, element);
            putTreePart(element,
                new AppTreeContextMenuPart(appTree, treePresenter,
                    (ContextMenuItemElement) element));
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElement(AbstractElement parent, AbstractElement element) {
        if ((element instanceof EventElement && handledElement.getEvents().contains(element))
            || (element instanceof ContextMenuItemElement
            && handledElement.hasChild((ContextMenuItemElement) element))) {
            treePresenter.riseRemoveElement(handledElement, element, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElementUI(AbstractElement parent, AbstractElement element) {
        if ((element instanceof EventElement && handledElement.getEvents().contains(element))
            || (element instanceof ContextMenuItemElement
            && handledElement.hasChild((ContextMenuItemElement) element))) {
            removeElement(element);
            return true;
        }
        return false;
    }

    @Override
    public boolean doEditElement(AbstractElement element) {
        return false;
    }

    @Override
    public boolean doEditElementRights(AbstractElement element) {
        if (element == folders.eventFolder && !handledElement.getEvents().isEmpty()) {
            treePresenter.riseEditRights(handledElement.getEvents(),
                Collections.singleton(RightType.EXECUTE));
            return true;
        }
        return false;
    }

    @Override
    public boolean canDragDrop(AbstractElement dropTarget, Object dropData) {
        if (dropTarget == folders.eventFolder && dropData instanceof EventElement) {
            return true;
        } else {
            return dropData instanceof ContextMenuItemElement
                && dropTarget instanceof ContextMenuItemElement
                && handledElement.hasChild((ContextMenuItemElement) dropData)
                && (dropTarget == handledElement
                || handledElement.hasChild((ContextMenuItemElement) dropTarget));
        }
    }

    @Override
    public boolean doDragDrop(AbstractElement dropTarget, Object dropData) {
        if ((dropTarget == folders.eventFolder || dropTarget == handledElement)
            && dropData instanceof EventElement) {
            treePresenter.riseAddElement(handledElement, (EventElement) dropData);
            return true;
        } else if (dropData instanceof ContextMenuItemElement
            && dropTarget instanceof ContextMenuItemElement
            && handledElement.hasChild((ContextMenuItemElement) dropData)
            && handledElement.hasChild((ContextMenuItemElement) dropTarget)) {
            ContextMenuItemElement childItem = (ContextMenuItemElement) dropData;
            childItem.setIndex(((ContextMenuItemElement) dropTarget).getIndex());

            treePresenter.riseAddElement(handledElement, childItem);

            appTree.setExpanded(handledElement, true);
            return true;
        } else if (dropData instanceof ContextMenuItemElement
            && dropTarget instanceof ContextMenuItemElement
            && handledElement.hasChild((ContextMenuItemElement) dropData)
            && handledElement == dropTarget) {
            ContextMenuItemElement childItem = (ContextMenuItemElement) dropData;
            childItem.setIndex(handledElement.getChildren().size());

            treePresenter.riseAddElement(handledElement, childItem);

            appTree.setExpanded(handledElement, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean isCopying(AbstractElement element) {
        return element instanceof ContextMenuItemElement;
    }

    @Override
    public boolean isPasting(AbstractElement parent, AbstractElement element) {
        return (parent == folders.eventFolder && element instanceof EventElement)
            || (parent == handledElement && element instanceof ContextMenuItemElement);
    }

    private class ContextMenuFolders {
        private DummyMenuItemEvents eventFolder;

        public ContextMenuFolders() {
            eventFolder = new DummyMenuItemEvents(handledElement.getId());
        }
    }
}
