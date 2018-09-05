package org.whirlplatform.editor.client.tree.part;

import org.whirlplatform.editor.client.meta.NewEventElement;
import org.whirlplatform.editor.client.meta.NewEventParameterElement;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.editor.client.tree.dummy.DummyEventParameters;
import org.whirlplatform.editor.client.tree.dummy.DummyEventSubEvents;
import org.whirlplatform.meta.shared.editor.*;

import java.util.Collections;

public class AppTreeEventPart extends AbstractAppTreePart<EventElement> {

    private class EventFolders {
        private DummyEventParameters parameterFolder;
        private DummyEventSubEvents subEventFolder;

        private EventFolders(EventElement event) {
            final String eventId = event.getId();
            parameterFolder = new DummyEventParameters(eventId);
            subEventFolder = new DummyEventSubEvents(eventId);
        }
    }

    private EventFolders folders;

    public AppTreeEventPart(AppTree appTree, AppTreePresenter treePresenter, EventElement event) {
        super(appTree, treePresenter, event);
        folders = new EventFolders(event);
    }

    @Override
    public void init() {
        addChildElement(handledElement, folders.parameterFolder);
        for (EventParameterElement p : handledElement.getParameters()) {
            treePresenter.riseAddElementUI(handledElement, p);
        }

        addChildElement(handledElement, folders.subEventFolder);
        for (EventElement e : handledElement.getSubEvents()) {
            treePresenter.riseAddElementUI(handledElement, e);
        }
    }

    @Override
    public boolean isRenaming(AbstractElement element) {
        return handledElement == element
                || (element instanceof EventParameterElement && handledElement.getParameters().contains(element));
    }

    @Override
    public boolean isAdding(AbstractElement element) {
        return element == folders.subEventFolder || element == folders.parameterFolder;
    }

    @Override
    public boolean isCopying(AbstractElement element) {
        return element instanceof EventElement;
    }

    @Override
    public boolean isPasting(AbstractElement parent, AbstractElement element) {
        return parent == folders.subEventFolder && element instanceof EventElement;
    }

    @Override
    public boolean isEditing(AbstractElement element) {
        return element == handledElement
                || (element instanceof EventParameterElement && handledElement.getParameters().contains(element));
    }

    @Override
    public boolean isDeleting(AbstractElement element) {
        return (element instanceof EventParameterElement && handledElement.getParameters().contains(element))
                || (handledElement instanceof EventElement && handledElement.getSubEvents().contains(element));
    }

    @Override
    public boolean hasRights(AbstractElement element) {
        return element == handledElement || element == folders.subEventFolder;
    }

    @Override
    public boolean doAddElement(AbstractElement parent, AbstractElement element) {
        if (parent == folders.parameterFolder && element == null) {
            treePresenter.riseAddElement(handledElement, new NewEventParameterElement());
            return true;
        } else if (parent == folders.subEventFolder && element == null) {
            treePresenter.riseAddElement(handledElement, new NewEventElement());
            return true;
        } else if (parent == folders.subEventFolder && element instanceof EventElement) {
            treePresenter.riseAddElement(handledElement, element);
            return true;
        }
        return false;
    }

    @Override
    public boolean doAddElementUI(AbstractElement parent, AbstractElement element) {
        if ((parent == handledElement || parent == folders.subEventFolder) && element instanceof EventElement) {
            // добавляем подчиненные события
            removeElement(element);
            addChildElement(folders.subEventFolder, element);
            putTreePart(element, new AppTreeEventPart(appTree, treePresenter, (EventElement) element));
            return true;
        } else if ((parent == handledElement || parent == folders.parameterFolder) && element instanceof EventParameterElement) {
            // добавляем параметры
            removeElement(element);
            addChildElement(folders.parameterFolder, element);
            for (EventParameterElement param : handledElement.getParameters()) {
                appTree.refresh(param);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElement(AbstractElement parent, AbstractElement element) {
        if ((element instanceof EventElement && handledElement.getSubEvents().contains(element))
                || (element instanceof EventParameterElement && handledElement.getParameters().contains(element))) {
            treePresenter.riseRemoveElement(handledElement, element, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElementUI(AbstractElement parent, AbstractElement element) {
        if ((element instanceof EventParameterElement && appTree.hasChild(folders.parameterFolder, element, false))
                || (element instanceof EventElement && appTree.hasChild(folders.subEventFolder, element, false))) {
            // удаляем параметры или подчиненные события
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
            treePresenter.riseEditRights(Collections.singleton(element),
                    Collections.singleton(RightType.EXECUTE));
            return true;
        } else if (element == folders.subEventFolder) {
            if (!handledElement.getSubEvents().isEmpty()) {
                treePresenter.riseEditRights(handledElement.getSubEvents(), Collections.singleton(RightType.EXECUTE));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean canDragDrop(AbstractElement dropTarget, Object dropData) {
        if ((dropTarget instanceof EventParameterElement && handledElement.getParameters().contains(dropTarget))
                && (dropData instanceof EventParameterElement && handledElement.getParameters().contains(dropData))) {
            return true;
        } else if (dropTarget == folders.parameterFolder && dropData instanceof EventParameterElement
                && handledElement.getParameters().contains(dropData)) {
            return true;
        } else if (dropData instanceof ComponentElement
                && (dropTarget == folders.parameterFolder || dropTarget instanceof EventParameterElement)) {
            return true;
        } else
            return dropData instanceof EventElement && (dropTarget == folders.subEventFolder || dropTarget == handledElement)
                    && dropData != handledElement && !handledElement.getSubEvents().contains(dropData)
                    && !((EventElement) dropData).getSubEvents().contains(handledElement);
    }

    @Override
    public boolean doDragDrop(AbstractElement dropTarget, Object dropData) {
        if ((dropTarget instanceof EventParameterElement && handledElement.getParameters().contains(dropTarget))
                && (dropData instanceof EventParameterElement && handledElement.getParameters().contains(dropData))) {
            // кинули параметр на другой параметр, просто меняем индекс
            EventParameterElement overParameter = (EventParameterElement) dropTarget;
            EventParameterElement parameter = (EventParameterElement) dropData;
            handledElement.setParameterIndex(parameter, overParameter.getIndex());

            treePresenter.riseRemoveElementUI(handledElement, overParameter);
            treePresenter.riseRemoveElementUI(handledElement, parameter);

            addChildElement(folders.parameterFolder, parameter);
            addChildElement(folders.parameterFolder, overParameter);
            appTree.setExpanded(folders.parameterFolder, true);
            for (EventParameterElement param : handledElement.getParameters()) {
                appTree.refresh(param);
            }
            return true;
        } else if (dropTarget == folders.parameterFolder && dropData instanceof EventParameterElement
                && handledElement.getParameters().contains(dropData)) {
            // кинули параметр в ветку параметров, добавляем параметр в конец
            EventParameterElement parameter = (EventParameterElement) dropData;

            treePresenter.riseRemoveElementUI(handledElement, parameter);

            handledElement.removeParameter(parameter);
            handledElement.addParameter(parameter);

            addChildElement(folders.parameterFolder, parameter);
            appTree.setExpanded(folders.parameterFolder, true);
            for (EventParameterElement param : handledElement.getParameters()) {
                appTree.refresh(param);
            }
            return true;
        } else if (dropTarget == folders.parameterFolder && dropData instanceof ComponentElement) {
            NewEventParameterElement e = new NewEventParameterElement();
            e.setComponentId(((ComponentElement) dropData).getId());
            treePresenter.riseAddElement(handledElement, e);
            return true;
        } else if ((dropTarget instanceof EventParameterElement && handledElement.getParameters().contains(dropTarget))
                && dropData instanceof ComponentElement) {
            EventParameterElement overParameter = (EventParameterElement) dropTarget;
            NewEventParameterElement e = new NewEventParameterElement();
            e.setIndex(overParameter.getIndex());
            e.setComponentId(((ComponentElement) dropData).getId());
            treePresenter.riseAddElement(handledElement, e);

            // treeHandler.getEventBus().removeElementUI(event, overParameter);
            // view.addChildElement(folders.parameterFolder, overParameter);
            return true;
        } else if ((dropTarget == handledElement || dropTarget == folders.subEventFolder) && dropData instanceof EventElement) {
            treePresenter.riseAddElement(handledElement, (EventElement) dropData);
            return true;
        }
        return false;
    }
}
