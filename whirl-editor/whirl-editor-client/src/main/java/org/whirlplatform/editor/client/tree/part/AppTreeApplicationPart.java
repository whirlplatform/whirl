package org.whirlplatform.editor.client.tree.part;

import com.google.gwt.core.client.Callback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.whirlplatform.component.client.utils.InfoHelper;
import org.whirlplatform.editor.client.meta.NewComponentElement;
import org.whirlplatform.editor.client.meta.NewDataSourceElement;
import org.whirlplatform.editor.client.meta.NewEventElement;
import org.whirlplatform.editor.client.meta.NullFreeComponentElement;
import org.whirlplatform.editor.client.meta.NullRootComponentElement;
import org.whirlplatform.editor.client.tree.AppTree;
import org.whirlplatform.editor.client.tree.AppTreePresenter;
import org.whirlplatform.editor.client.tree.dummy.DummyAppComponents;
import org.whirlplatform.editor.client.tree.dummy.DummyAppDataSources;
import org.whirlplatform.editor.client.tree.dummy.DummyAppEvents;
import org.whirlplatform.editor.client.tree.dummy.DummyAppFreeComponents;
import org.whirlplatform.editor.client.tree.dummy.DummyAppGroups;
import org.whirlplatform.editor.client.tree.dummy.DummyAppLocales;
import org.whirlplatform.editor.client.tree.dummy.DummyAppReferences;
import org.whirlplatform.editor.shared.EditorDataService;
import org.whirlplatform.meta.shared.ApplicationStoreData;
import org.whirlplatform.meta.shared.component.ComponentType;
import org.whirlplatform.meta.shared.editor.AbstractElement;
import org.whirlplatform.meta.shared.editor.ApplicationElement;
import org.whirlplatform.meta.shared.editor.ComponentElement;
import org.whirlplatform.meta.shared.editor.EventElement;
import org.whirlplatform.meta.shared.editor.FormElement;
import org.whirlplatform.meta.shared.editor.ReportElement;
import org.whirlplatform.meta.shared.editor.RightType;
import org.whirlplatform.meta.shared.editor.db.AbstractTableElement;
import org.whirlplatform.meta.shared.editor.db.DataSourceElement;

public class AppTreeApplicationPart extends AbstractAppTreePart<ApplicationElement> {

    private ApplicationFolders folders;

    public AppTreeApplicationPart(AppTree appTree, AppTreePresenter treePresenter,
                                  ApplicationElement application) {
        super(appTree, treePresenter, application);
        folders = new ApplicationFolders(application);
    }

    public void init() {
        // загружаем в дерево приложение
        addRootElement(handledElement);
        addChildElement(handledElement, folders.components);
        addChildElement(folders.components, folders.freeComponents);
        addChildElement(handledElement, folders.events);
        addChildElement(handledElement, folders.locales);
        addChildElement(handledElement, folders.datasources);
        addChildElement(handledElement, folders.groups);
        addChildElement(handledElement, folders.references);

        // загружаем корневой компонент
        if (handledElement.getRootComponent() != null) {
            doAddElementUI(handledElement, handledElement.getRootComponent());
        }

        // загружаем свободные компоненты
        for (ComponentElement c : handledElement.getFreeComponents()) {
            doAddElementUI(handledElement, c);
        }

        // загружаем события
        for (EventElement e : handledElement.getFreeEvents()) {
            doAddElementUI(handledElement, e);
        }

        // источники данных
        for (DataSourceElement d : handledElement.getDataSources()) {
            doAddElementUI(handledElement, d);
        }

        // Зависимости
        for (ApplicationElement a : handledElement.getReferences()) {
            doAddElementUI(handledElement, a);
        }
    }

    public ApplicationElement getApplication() {
        return handledElement;
    }

    @Override
    public boolean isRenaming(AbstractElement element) {
        return handledElement == element;
    }

    @Override
    public boolean isAdding(AbstractElement element) {
        return element == folders.events || element == folders.datasources
            || element == folders.references || isComponentAddable(element);
    }

    @Override
    public boolean isCopying(AbstractElement element) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isPasting(AbstractElement parent, AbstractElement element) {
        return (element instanceof ComponentElement
                && (parent == folders.components || parent == folders.freeComponents))
                || (element instanceof EventElement && parent == folders.events);
    }

    @Override
    public boolean isEditing(AbstractElement element) {
        return false;
    }

    @Override
    public boolean hasRights(AbstractElement element) {
        return element == folders.components || element == folders.freeComponents
            || element == folders.events || element == folders.datasources;
    }

    @Override
    public boolean isDeleting(AbstractElement element) {
        return element == handledElement.getRootComponent()
                || (element instanceof ComponentElement
                && handledElement.getFreeComponents().contains(element))
                || (element instanceof EventElement
                && handledElement.getFreeEvents().contains(element))
                || (element instanceof DataSourceElement
                && handledElement.getDataSources().contains(element));
    }

    @Override
    public boolean doAddElement(AbstractElement parent, AbstractElement element) {
        if (parent == folders.events && element == null) {
            treePresenter.riseAddElement(handledElement, new NewEventElement());
            return true;
        } else if (parent == folders.events && element instanceof EventElement) {
            treePresenter.riseAddElement(handledElement, element);
            return true;
        } else if (parent == folders.datasources && element == null) {
            treePresenter.riseAddElement(handledElement, new NewDataSourceElement());
            return true;
        } else if (parent == folders.references) {
            // Callback для добавления зависимости после загрузки
            final AsyncCallback<ApplicationElement> callback =
                    new AsyncCallback<ApplicationElement>() {
                        @Override
                        public void onSuccess(ApplicationElement result) {
                            treePresenter.riseAddElement(handledElement, result);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            InfoHelper.throwInfo("load-application", caught);
                        }
                    };

            // Callback для загрузки зависимости после получения её id
            treePresenter.riseShowOpenApplicationsCallback(
                    new Callback<ApplicationStoreData, Throwable>() {
                        @Override
                        public void onFailure(Throwable reason) {
                        }

                        @Override
                        public void onSuccess(ApplicationStoreData result) {
                            EditorDataService.Util.getDataService()
                                    .loadApplication(result, callback);
                        }
                    });
            return true;
        } else if (element instanceof ComponentElement && parent == folders.components) {
            treePresenter.riseAddElement(new NullRootComponentElement(), element);
            return true;
        } else if (element instanceof ComponentElement && parent == folders.freeComponents) {
            treePresenter.riseAddElement(new NullFreeComponentElement(), element);
            return true;
        }
        return false;
    }

    @Override
    public boolean doAddElementUI(AbstractElement parent, AbstractElement element) {
        if ((parent == handledElement || parent instanceof NullRootComponentElement)
                && element == handledElement.getRootComponent()) {
            removeElement(element);
            addChildElement(folders.components, element);
            putTreePart(element,
                    new AppTreeComponentPart(appTree, treePresenter, (ComponentElement) element));
            return true;
        } else if ((parent == handledElement || parent instanceof NullFreeComponentElement)
                && (element instanceof ComponentElement
                && handledElement.getFreeComponents().contains(element))) {
            removeElement(element);
            addChildElement(folders.freeComponents, element);
            putTreePart(element,
                    new AppTreeComponentPart(appTree, treePresenter, (ComponentElement) element));
            return true;
        } else if (parent == handledElement && element instanceof EventElement
                && handledElement.getFreeEvents().contains(element)) {
            removeElement(element);
            addChildElement(folders.events, element);
            putTreePart(element,
                    new AppTreeEventPart(appTree, treePresenter, (EventElement) element));
            return true;
        } else if (parent == handledElement && element instanceof DataSourceElement
                && handledElement.getDataSources().contains(element)) {
            removeElement(element);
            addChildElement(folders.datasources, element);
            putTreePart(element,
                    new AppTreeDataSourcePart(appTree, treePresenter, (DataSourceElement) element));
            return true;
        } else if (parent == handledElement && element instanceof ApplicationElement
                && handledElement.getReferences().contains(element)) {
            removeElement(element);
            addChildElement(folders.references, element);
            putTreePart(element,
                    new AppTreeReferencePart(appTree, treePresenter, (ApplicationElement) element));
            return true;
        } else if (parent == handledElement && element instanceof EventElement) {
            removeElement(element);
            addChildElement(folders.events, element);
            putTreePart(element,
                    new AppTreeEventPart(appTree, treePresenter, (EventElement) element));
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
    public boolean doRemoveElement(AbstractElement parent, AbstractElement element) {
        if (handledElement.getRootComponent() == element) {
            treePresenter.riseRemoveElement(new NullRootComponentElement(), element, true);
            return true;
        } else if (element instanceof ComponentElement
            && handledElement.getFreeComponents().contains(element)) {
            treePresenter.riseRemoveElement(new NullFreeComponentElement(), element, true);
            return true;
        } else if ((element instanceof EventElement
            && handledElement.getFreeEvents().contains(element))
            || (element instanceof DataSourceElement
            && handledElement.getDataSources().contains(element))) {
            treePresenter.riseRemoveElement(handledElement, element, true);
            return true;
        } else if (element instanceof ApplicationElement
            && handledElement.getReferences().contains(element)) {
            treePresenter.riseRemoveElement(handledElement, element, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean doRemoveElementUI(AbstractElement parent, AbstractElement element) {
        /* компонент */
        if ((element instanceof ComponentElement
                && (appTree.hasChild(folders.components, element, false)
                || appTree.hasChild(folders.freeComponents, element, false)))
                /* событие */
                || (element instanceof EventElement
                && appTree.hasChild(folders.events, element, false))
                /* источник данных */
                || (element instanceof DataSourceElement
                && appTree.hasChild(folders.datasources, element, false))
                /* зависимости */
                || (element instanceof ApplicationElement
                && appTree.hasChild(folders.references, element, false))) {
            removeElement(element);
            return true;
        }
        return false;
    }

    @Override
    public boolean doEditElementRights(AbstractElement element) {
        if (element == folders.events) {
            if (!handledElement.getFreeEvents().isEmpty()) {
                treePresenter.riseEditRights(handledElement.getFreeEvents(),
                        Collections.singleton(RightType.EXECUTE));
            }
            return true;
        } else if (element == folders.components) {
            Collection<EventElement> events =
                    getAllComponentsEvents(handledElement.getRootComponent());
            if (!events.isEmpty()) {
                treePresenter.riseEditRights(events, Collections.singleton(RightType.EXECUTE));
            }
            return true;
        } else if (element == folders.freeComponents) {
            Collection<EventElement> events = new HashSet<EventElement>();
            for (ComponentElement c : handledElement.getFreeComponents()) {
                events.addAll(getAllComponentsEvents(c));
            }
            if (!events.isEmpty()) {
                treePresenter.riseEditRights(events, Collections.singleton(RightType.EXECUTE));
            }
            return true;
        } else if (element == folders.datasources) {
            Collection<AbstractTableElement> tables = new HashSet<AbstractTableElement>();
            for (AbstractTableElement t : handledElement.getTablesWithClones()) {
                tables.add(t);
            }
            if (!tables.isEmpty()) {
                treePresenter.riseEditRights(tables,
                        Collections.unmodifiableCollection(Arrays.asList(RightType.ADD,
                                RightType.DELETE, RightType.EDIT, RightType.VIEW,
                                RightType.RESTRICT)));
            }
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
        if ((dropData instanceof ComponentType || dropData instanceof ComponentElement)
                && (dropTarget == folders.freeComponents || dropTarget == folders.components)) {
            return true;
        } else if (dropTarget instanceof ReportElement
                && (ComponentType.FormBuilderType.equals(dropData)
                || dropData instanceof FormElement)) {
            return true;
        } else {
            return dropData instanceof EventElement && dropTarget == folders.events;
        }
    }

    @Override
    public boolean doDragDrop(AbstractElement dropTarget, Object dropData) {
        if (dropTarget == folders.freeComponents && dropData instanceof ComponentType) {
            treePresenter.riseAddElement(new NullFreeComponentElement(),
                    new NewComponentElement((ComponentType) dropData));
            return true;
        } else if (dropTarget == folders.freeComponents && dropData instanceof ComponentElement) {
            treePresenter.riseAddElement(new NullFreeComponentElement(),
                    (ComponentElement) dropData);
        } else if (dropTarget == folders.components && dropData instanceof ComponentType) {
            treePresenter.riseAddElement(new NullRootComponentElement(),
                    new NewComponentElement((ComponentType) dropData));
            return true;
        } else if (dropTarget == folders.components && dropData instanceof ComponentElement) {
            treePresenter.riseAddElement(new NullRootComponentElement(),
                    (ComponentElement) dropData);
        } else if (dropTarget instanceof ReportElement
            && ComponentType.FormBuilderType.equals(dropData)) {
            treePresenter.riseAddElement(dropTarget,
                    new NewComponentElement((ComponentType) dropData));
            return true;
        } else if (dropTarget instanceof ReportElement && dropData instanceof FormElement) {
            treePresenter.riseAddElement(dropTarget, (ComponentElement) dropData);
            return true;
        } else if (dropTarget == folders.events && dropData instanceof EventElement) {
            treePresenter.riseAddElement(handledElement, (EventElement) dropData);
        }
        return false;
    }

    /**
     * Проверяет могут ли у компонента быть потомки
     *
     * @param element
     * @return
     */
    private boolean isComponentAddable(AbstractElement element) {
        if (element instanceof ComponentElement) {
            ComponentType type = ((ComponentElement) element).getType();
            return type.equals(ComponentType.HorizontalMenuType)
                || type.equals(ComponentType.ButtonType)
                || type.equals(ComponentType.HorizontalMenuItemType)
                || type.equals(ComponentType.TreeMenuType);
        }
        return false;
    }

    class ApplicationFolders {
        private DummyAppComponents components;
        private DummyAppFreeComponents freeComponents;
        private DummyAppEvents events;
        private DummyAppDataSources datasources;
        private DummyAppLocales locales;
        private DummyAppGroups groups;
        private DummyAppReferences references;

        private ApplicationFolders(ApplicationElement application) {
            // id используется в ApplicationTreeComparator
            final String appId = application.getId();
            components = new DummyAppComponents(appId);
            freeComponents = new DummyAppFreeComponents(appId);
            events = new DummyAppEvents(appId);
            datasources = new DummyAppDataSources(appId);
            locales = new DummyAppLocales(appId);
            groups = new DummyAppGroups(appId);
            references = new DummyAppReferences(appId);
        }
    }
}
